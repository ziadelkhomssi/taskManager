package com.ilemgroup.internship.taskmanager.backend;

import com.ilemgroup.internship.taskmanager.backend.entity.*;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestEntityFactory {
    static public Project createBaseProject(
    ) {
        Project project = new Project();
        project.setTitle("Project");
        project.setDescription("Description");
        project.setProfilePicturePath("profilePicture.png");
        project.setStatus(ProjectStatus.ACTIVE);
        return project;
    }

    static public Sprint createBaseSprint(Project parentProject) {
        Sprint sprint = new Sprint();
        sprint.setTitle("Sprint");
        sprint.setDescription("Description");
        sprint.setStatus(SprintStatus.ACTIVE);
        sprint.setStartDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        sprint.setDueDate(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        sprint.setProject(parentProject);
        return sprint;
    }

    static public Ticket createBaseTicket(Sprint parentSprint, User assignedUser) {
        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket");
        ticket.setDescription("Description");
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setSprint(parentSprint);
        ticket.setUser(assignedUser);
        return ticket;
    }

    static public TicketComment createBaseTicketComment(Ticket ticket, User user) {
        TicketComment comment = new TicketComment();
        comment.setComment("Comment");
        comment.setUser(user);
        comment.setTicket(ticket);
        return comment;
    }

    static public User createBaseUser() {
        User user = new User();
        user.setAzureOid("abc123");
        user.setName("John Developer");
        user.setEmail("johndo@email.com");
        user.setJob("Engineer");
        return user;
    }

    static public Notification createBaseNotification(Ticket assignedTicket) {
        Notification notification = new Notification();
        notification.setType(NotificationType.TICKET_ASSIGNED);
        notification.setTicket(assignedTicket);
        return notification;
    }
}
