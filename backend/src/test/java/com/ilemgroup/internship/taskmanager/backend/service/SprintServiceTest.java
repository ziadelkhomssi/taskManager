package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SprintServiceTest {
    @Autowired
    private SprintService sprintService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        projectRepository.deleteAll();
        sprintRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetDetails() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        SprintDetails details = sprintService.getDetailsById(sprint.getId());

        assertNotNull(details);
        assertEquals(sprint.getTitle(), details.title());
        assertEquals(sprint.getStatus(), details.status());
    }

    @Test
    void testGetDetails_NotFound() {
        assertThrows(EntityNotFoundException.class,
                () -> sprintService.getDetailsById(999L));
    }

    @Test
    void testGetSummaryList_FilterBySprint() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = TestEntityFactory.createBaseSprint(project);
        Sprint sprint2 = TestEntityFactory.createBaseSprint(project);
        sprint1.setTitle("Alpha");
        sprint2.setTitle("Beta");
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint2);

        PageQuery query = new PageQuery(0, 10, "alp", "sprint");

        List<SprintSummary> result = sprintService.getSummaryList(query);

        assertEquals(1, result.size());
        assertEquals(sprint1.getTitle(), result.getFirst().title());
    }

    @Test
    void testGetSummaryList_FilterByStatus() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = TestEntityFactory.createBaseSprint(project);
        Sprint sprint2 = TestEntityFactory.createBaseSprint(project);
        sprint1.setStatus(SprintStatus.PLANNED);
        sprint2.setStatus(SprintStatus.ACTIVE);
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint2);

        PageQuery query = new PageQuery(0, 10, "PLANNED", "status");

        List<SprintSummary> result = sprintService.getSummaryList(query);

        assertEquals(1, result.size());
        assertEquals(sprint1.getStatus(), result.getFirst().status());
    }

    @Test
    void testGetSummaryList_FilterByStatus_Invalid() {
        PageQuery query = new PageQuery(0, 10, "NOT_A_REAL_STATUS", "status");

        assertThrows(ResponseStatusException.class, () -> sprintService.getSummaryList(query));
    }

    @Test
    void testGetSummaryList_UnknownFilter() {
        PageQuery query = new PageQuery(0, 10, "x", "unknown_filter");

        assertThrows(ResponseStatusException.class, () -> sprintService.getSummaryList(query));
    }

    @Test
    void testGetSummaryList_FilterByUser() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        User user = TestEntityFactory.createBaseUser();
        user.setName("John Developer");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        PageQuery query = new PageQuery(0, 10, "john", "user");

        List<SprintSummary> result = sprintService.getSummaryList(query);

        assertEquals(1, result.size());
        assertEquals(sprint.getTitle(), result.getFirst().title());
    }

    @Test
    void testCreateSprint() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());

        SprintCreate command = new SprintCreate(
                "New Sprint", "Description", 
                LocalDate.now(), LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE, project.getId()
        );

        sprintService.createSprint(command);

        List<Sprint> all = sprintRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(command.title(), all.getFirst().getTitle());
    }

    @Test
    void testUpdateSprint() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint old = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        SprintUpdate command = new SprintUpdate(
                old.getId(),
                "Updated Name", 
                "New Description",
                LocalDate.now(), LocalDate.now().plusDays(7),
                SprintStatus.PLANNED
        );

        sprintService.updateSprint(command);

        Sprint updated = sprintRepository.findById(old.getId()).orElseThrow();

        assertEquals(command.title(), updated.getTitle());
        assertEquals(command.description(), updated.getDescription());
        assertEquals(SprintStatus.PLANNED, updated.getStatus());
    }

    @Test
    void testUpdateSprint_NotFound() {
        SprintUpdate command = new SprintUpdate(
                999L, "X", "Y",
                LocalDate.now(), LocalDate.now().plusDays(7),
                SprintStatus.PLANNED
        );

        assertThrows(EntityNotFoundException.class, () -> sprintService.updateSprint(command));
    }

    @Test
    void testDeleteSprint() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint existing = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        sprintService.deleteSprintById(existing.getId());

        assertFalse(sprintRepository.existsById(existing.getId()));
    }

    @Test
    void testDeleteSprint_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> sprintService.deleteSprintById(999L));
    }
}
