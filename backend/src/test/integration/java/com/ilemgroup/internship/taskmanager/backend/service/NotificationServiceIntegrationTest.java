package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.*;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceIntegrationTest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        sprintRepository.deleteAll();
        projectRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testGetDetailsList() throws AccessDeniedException {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        Notification notification1 = TestEntityFactory.createBaseNotification(ticket);
        Notification notification2 = TestEntityFactory.createBaseNotification(ticket);
        notification1.setType(NotificationType.TICKET_ASSIGNED);
        notification2.setType(NotificationType.TICKET_STATUS_CHANGED);
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<NotificationDetails> result = notificationService.getDetailsList(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(notification1.getType(), result.getContent().get(0).type());
        assertEquals(notification2.getType(), result.getContent().get(1).type());
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testMarkAsRead() throws AccessDeniedException {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        Notification unreadNotification = notificationRepository.save(TestEntityFactory.createBaseNotification(ticket));

        notificationService.markAsRead(unreadNotification.getId());

        Notification readNotification = notificationRepository.findById(unreadNotification.getId()).orElseThrow();
        assertTrue(readNotification.isRead());
    }

    @Test
    @WithMockUser(username = "notabc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testMarkAsRead_AccessDenied() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = TestEntityFactory.createBaseUser();
        user.setAzureOid("abc123");
        userRepository.save(user);

        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));
        Notification unreadNotification = notificationRepository.save(TestEntityFactory.createBaseNotification(ticket));

        assertThrows(AccessDeniedException.class, () -> notificationService.markAsRead(unreadNotification.getId()));
    }

    @Test
    void testMarkAsRead_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(999L));
    }

    @Test
    void testCreateNotification() {
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        Ticket ticket = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint, user));

        notificationService.createNotification(ticket.getId(), NotificationType.TICKET_ASSIGNED);

    }

    @Test
    void testCreateNotification_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> notificationService.createNotification(
                999L, NotificationType.TICKET_ASSIGNED
        ));
    }
}
