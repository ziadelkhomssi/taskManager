package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketServiceIntegrationTest {
    @Autowired
    private TicketService ticketService;

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
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        sprintRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void testGetDetails() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        TicketDetails details = ticketService.getDetailsById(ticket.getId());

        assertNotNull(details);
        assertEquals(ticket.getTitle(), details.title());
        assertEquals(ticket.getStatus(), details.status());
    }

    @Test
    void testGetDetails_NotFound() {
        assertThrows(EntityNotFoundException.class,
                () -> ticketService.getDetailsById(999L));
    }

    @Test
    void testGetTicketSummaryList_BlankSearch() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint = sprintRepository.save(
                TestEntityFactory.createBaseSprint(project)
        );

        User user = userRepository.save(
                TestEntityFactory.createBaseUser()
        );

        Ticket ticket1 = TestEntityFactory.createBaseTicket(sprint, user);
        Ticket ticket2 = TestEntityFactory.createBaseTicket(sprint, user);
        ticket1.setTitle("Bug 101");
        ticket2.setTitle("Bug 102 :O");
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TicketSummary> result =
                ticketService.getSummaryList(
                        sprint.getId(),
                        pageable,
                        "",
                        "ticket"
                );

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetTicketSummaryList_FilterByTicket() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint = sprintRepository.save(
                TestEntityFactory.createBaseSprint(project)
        );

        User user = userRepository.save(
                TestEntityFactory.createBaseUser()
        );

        Ticket ticket1 = TestEntityFactory.createBaseTicket(sprint, user);
        Ticket ticket2 = TestEntityFactory.createBaseTicket(sprint, user);
        ticket1.setTitle("Bug 101");
        ticket2.setTitle("Bug 102 :O");
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TicketSummary> result =
                ticketService.getSummaryList(
                        sprint.getId(),
                        pageable,
                        "101",
                        "ticket"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(ticket1.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetTicketSummaryList_FilterByStatus() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint = sprintRepository.save(
                TestEntityFactory.createBaseSprint(project)
        );

        User user = userRepository.save(
                TestEntityFactory.createBaseUser()
        );

        Ticket ticket1 = TestEntityFactory.createBaseTicket(sprint, user);
        Ticket ticket2 = TestEntityFactory.createBaseTicket(sprint, user);
        ticket1.setStatus(TicketStatus.IN_PROGRESS);
        ticket2.setStatus(TicketStatus.IN_TESTING);
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TicketSummary> result =
                ticketService.getSummaryList(
                        sprint.getId(),
                        pageable,
                        "IN_PROGRESS",
                        "status"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(ticket1.getStatus(), result.getContent().getFirst().status());
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

        Page<TicketSummary> result =
                ticketService.getSummaryList(
                        sprint.getId(),
                        pageable,
                        "john",
                        "user"
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(ticket.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetTicketSummaryList_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(
                ResponseStatusException.class,
                () -> ticketService.getSummaryList(
                        1L,
                        pageable,
                        "x",
                        "unknown_filter"
                )
        );
    }

    @Test
    void testCreateTicket() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());

        List<Sprint> allSprints = sprintRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        TicketCreate command = new TicketCreate(
                "New Ticket", "Description", user.getAzureOid(),
                TicketPriority.MEDIUM, TicketStatus.BACKLOG, sprint.getId()
        );

        ticketService.createTicket(command);

        List<Ticket> all = ticketRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(command.title(), all.getFirst().getTitle());
    }

    @Test
    void testUpdateTicket() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        Ticket old = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        TicketUpdate command = new TicketUpdate(
                old.getId(),
                "Updated Name",
                "New Description",
                "abc123",
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS
        );

        ticketService.updateTicket(command);

        Ticket updated = ticketRepository.findById(old.getId()).orElseThrow();

        assertEquals(command.title(), updated.getTitle());
        assertEquals(command.description(), updated.getDescription());
        assertEquals(TicketStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void testUpdateTicket_NotFound() {
        TicketUpdate command = new TicketUpdate(
                999L, "X", "Y",
                "abc123",
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS
        );

        assertThrows(EntityNotFoundException.class, () -> ticketService.updateTicket(command));
    }

    @Test
    void testDeleteTicket() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        Ticket existing = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        ticketService.deleteTicketById(existing.getId());

        assertFalse(ticketRepository.existsById(existing.getId()));
    }

    @Test
    void testDeleteTicket_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> ticketService.deleteTicketById(999L));
    }
}
