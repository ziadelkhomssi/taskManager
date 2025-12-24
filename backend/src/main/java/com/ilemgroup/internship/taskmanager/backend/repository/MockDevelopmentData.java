package com.ilemgroup.internship.taskmanager.backend.repository;

/*
Class specifically for adding mock data in development run
*/

import com.ilemgroup.internship.taskmanager.backend.entity.*;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

@Configuration
@Profile("development")
public class MockDevelopmentData implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketCommentRepository ticketCommentRepository;
    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public void run(String... args) throws Exception {
        User user1 = new User(
                "abc123",
                "John Developer",
                "johndo@company.com",
                "Backend Developer",
                "static/image/user/mock/user_john_profile_picture.png",
                null,
                null
        );

        User user2 = new User(
                "def456",
                "Jane Developer",
                "janedo@company.com",
                "Frontend Developer",
                "static/image/user/mock/user_jane_profile_picture.png",
                null,
                null
        );

        userRepository.saveAll(List.of(user1, user2));


        Project project1 = new Project(
                null,
                "Task Manager",
                "Spring boot / Angular application to manage task assignement inside a company",
                null,
                ProjectStatus.ACTIVE,
                null
        );
        Project project2 = new Project(
                null,
                "Not Task Manager",
                "Literally not Task Manager",
                null,
                ProjectStatus.ARCHIVED,
                null
        );

        projectRepository.saveAll(List.of(
                project1,
                project2
        ));


        Sprint sprint1 = new Sprint(
                null,
                "Week 1",
                "First week sprint",
                LocalDateTime.of(2025, Month.DECEMBER, 2, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 2, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 2, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                SprintStatus.DONE,
                project1,
                null
        );
        Sprint sprint2 = new Sprint(
                null,
                "Week 2",
                "Second week sprint",
                LocalDateTime.of(2025, Month.DECEMBER, 9, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 9, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 9, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                SprintStatus.DONE,
                project1,
                null
        );
        Sprint sprint3 = new Sprint(
                null,
                "Week 3",
                "Third week sprint",
                LocalDateTime.of(2025, Month.DECEMBER, 16, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 16, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                null,
                SprintStatus.ACTIVE,
                project1,
                null
        );
        Sprint sprint4 = new Sprint(
                null,
                "Week 4",
                "Fourth week sprint",
                LocalDateTime.of(2025, Month.DECEMBER, 23, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.of(2025, Month.DECEMBER, 23, 0, 0, 0).plusDays(7).atZone(ZoneId.systemDefault()).toInstant(),
                null,
                SprintStatus.PLANNED,
                project1,
                null
        );

        sprintRepository.saveAll(List.of(
                sprint1,
                sprint2,
                sprint3,
                sprint4
        ));


        Ticket ticket1 = new Ticket(
                null,
                "Make Mock Data",
                "Mock data for testing in development",
                sprint3,
                user1,
                TicketPriority.HIGH,
                TicketStatus.IN_PROGRESS,
                null,
                null,
                null,
                null
        );

        Ticket ticket2 = new Ticket(
                null,
                "Manually Make Services",
                "Autogenerated OpenAPI services suck, make them manually instead.",
                sprint3,
                user2,
                TicketPriority.MEDIUM,
                TicketStatus.BACKLOG,
                null,
                null,
                null,
                null
        );

        ticketRepository.saveAll(List.of(
                ticket1, 
                ticket2
        ));


        TicketComment comment1 = new TicketComment(
                null,
                user2,
                "OMG, I'M JANE DOE!!",
                ticket1,
                null,
                null,
                LocalDateTime.now().minusHours(3),
                null
        );

        TicketComment reply1 = new TicketComment(
                null,
                user1,
                "Hello Jane Doe, I am John Doe. Strange isn't it??",
                ticket1,
                comment1,
                null,
                LocalDateTime.now().minusHours(2),
                null
        );

        comment1.setReplies(List.of(reply1));

        ticketCommentRepository.saveAll(List.of(
                comment1,
                reply1
        ));


        Notification notification = new Notification(
                null,
                NotificationType.TICKET_ASSIGNED,
                ticket1,
                false,
                null
        );

        notificationRepository.save(notification);
    }
}
