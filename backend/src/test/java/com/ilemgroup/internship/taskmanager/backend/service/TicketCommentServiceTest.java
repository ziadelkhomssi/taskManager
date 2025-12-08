package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.*;
import com.ilemgroup.internship.taskmanager.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketCommentServiceTest {
    @Autowired
    private TicketCommentService ticketCommentService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @BeforeEach
    void cleanDatabase() {
        projectRepository.deleteAll();
        sprintRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        ticketCommentRepository.deleteAll();
    }

    @Test
    void getDetailsList() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        TicketComment ticketComment1 = TestEntityFactory.createBaseTicketComment(ticket, user);
        TicketComment ticketComment2 = TestEntityFactory.createBaseTicketComment(ticket, user);

        ticketComment2.setParentComment(ticketComment1);
        ticketComment1 = ticketCommentRepository.save(ticketComment1);
        ticketComment2 = ticketCommentRepository.save(ticketComment2);

        PageQuery query = new PageQuery(0, 10, null, null);
        List<TicketCommentDetails> result = ticketCommentService.getDetailsList(ticket.getId(), query);

        assertEquals(2, result.size());
        assertEquals(ticketComment1.getComment(), result.get(0).comment());
        assertEquals(ticketComment2.getParentComment().getId(), result.get(1).parentCommentId());
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testCreateTicketComment() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        TicketCommentCreate command = new TicketCommentCreate(
                "HELLO", ticket.getId(), null
        );

        ticketCommentService.createTicketComment(command);

        List<TicketComment> all = ticketCommentRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(command.comment(), all.getFirst().getComment());
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testUpdateTicketComment() throws AccessDeniedException {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);
        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        TicketComment old = ticketCommentRepository.save(
                TestEntityFactory.createBaseTicketComment(ticket, user)
        );

        TicketCommentUpdate command = new TicketCommentUpdate(
                old.getId(), "HELLO BUT NEWER"
        );

        ticketCommentService.updateTicketComment(command);

        TicketComment updated = ticketCommentRepository.findById(old.getId()).orElseThrow();
        assertEquals(updated.getComment(), command.comment());
    }

    @Test
    @WithMockUser(username = "notabc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testUpdateTicketComment_AccessDenied() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        TicketComment old = ticketCommentRepository.save(
                TestEntityFactory.createBaseTicketComment(ticket, user)
        );

        TicketCommentUpdate command = new TicketCommentUpdate(
                old.getId(), "HELLO BUT NEWER"
        );

        assertThrows(AccessDeniedException.class, () -> ticketCommentService.updateTicketComment(command));
    }

    @Test
    void testUpdateTicketComment_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> ticketCommentService.updateTicketComment(
                new TicketCommentUpdate(
                        999L, "HELLO BUT NEVER"
                )
        ));
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testDeleteTicketComment() throws AccessDeniedException {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        TicketComment existing = ticketCommentRepository.save(
                TestEntityFactory.createBaseTicketComment(ticket, user)
        );

        ticketCommentService.deleteTicketCommentById(existing.getId());

        assertFalse(ticketCommentRepository.existsById(existing.getId()));
    }

    @Test
    @WithMockUser(username = "notabc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testDeleteTicketComment_AccessDenied() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        TicketComment old = ticketCommentRepository.save(
                TestEntityFactory.createBaseTicketComment(ticket, user)
        );

        assertThrows(AccessDeniedException.class, () -> ticketCommentService.deleteTicketCommentById(old.getId()));
    }

    @Test
    void testDeleteTicketComment_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> ticketCommentService.deleteTicketCommentById(999L));
    }
}
