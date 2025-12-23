package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SprintServiceIntegrationTest {
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
    void testGetSprintSummaryList_BlankSearch() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint1 = TestEntityFactory.createBaseSprint(project);
        Sprint sprint2 = TestEntityFactory.createBaseSprint(project);
        sprint1.setTitle("Alpha");
        sprint2.setTitle("Beta");
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<SprintSummary> result =
                sprintService.getSummaryList(
                        project.getId(),
                        pageable,
                        "",
                        "sprint"
                );

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetSprintSummaryList_FilterBySprint() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint1 = TestEntityFactory.createBaseSprint(project);
        Sprint sprint2 = TestEntityFactory.createBaseSprint(project);
        sprint1.setTitle("Alpha");
        sprint2.setTitle("Beta");
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<SprintSummary> result =
                sprintService.getSummaryList(
                        project.getId(),
                        pageable,
                        "alp",
                        "sprint"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(sprint1.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetSprintSummaryList_FilterByStatus() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint1 = TestEntityFactory.createBaseSprint(project);
        Sprint sprint2 = TestEntityFactory.createBaseSprint(project);
        sprint1.setStatus(SprintStatus.PLANNED);
        sprint2.setStatus(SprintStatus.ACTIVE);
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<SprintSummary> result =
                sprintService.getSummaryList(
                        project.getId(),
                        pageable,
                        "plan",
                        "status"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(sprint1.getStatus(), result.getContent().getFirst().status());
    }

    @Test
    void testGetSummaryList_FilterByUser() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        User user = TestEntityFactory.createBaseUser();
        user.setName("John Developer");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        Pageable pageable = PageRequest.of(0, 10);

        Page<SprintSummary> result =
                sprintService.getSummaryList(
                        project.getId(),
                        pageable,
                        "john",
                        "user"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(sprint.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetSprintSummaryList_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(
                ResponseStatusException.class,
                () -> sprintService.getSummaryList(1L, pageable, "x", "unknown_filter")
        );
    }

    @Test
    void testCreateSprint() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());

        SprintCreate command = new SprintCreate(
                "New Sprint", "Description",
                Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS), null,
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
                Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS), null,
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
                Instant.now(), Instant.now().plus(7, ChronoUnit.DAYS), null,
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
