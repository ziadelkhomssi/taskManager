package com.ilemgroup.internship.taskmanager.backend;

import com.ilemgroup.internship.taskmanager.backend.entity.*;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;

import java.time.LocalDate;

public class TestEntityFactory {
    static public Project createBaseProject(
    ) {
        Project project = new Project();
        project.setTitle("Project");
        project.setDescription("Description");
        project.setStatus(ProjectStatus.ACTIVE);
        return project;
    }

    static public Sprint createBaseSprint(Project parentProject) {
        Sprint sprint = new Sprint();
        sprint.setTitle("Sprint");
        sprint.setDescription("Description");
        sprint.setStatus(SprintStatus.ACTIVE);
        sprint.setStartDate(LocalDate.now());
        sprint.setDueDate(LocalDate.now().plusDays(7));
        sprint.setProject(parentProject);
        return sprint;
    }

    static public Ticket createBaseTicket(Sprint parentSprint, User assignedUser) {
        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket");
        ticket.setDescription("Description");
        ticket.setStatus(TicketStatus.IN_PROGRESS);
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
