package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

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
    void testGetDetailsById() {
        User user = userRepository.save(TestEntityFactory.createBaseUser());
        UserDetails userDetails = userService.getDetailsById(user.getAzureOid());

        assertEquals(userDetails.id(), user.getAzureOid());
        assertEquals(userDetails.name(), user.getName());
    }

    @Test
    void testGetSummaryList_FilterByName() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setName("Jane Developer");
        user2.setName("Janice Developer");
        user3.setName("John Developer");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        PageQuery query = new PageQuery(0, 10, "jan", "name");

        PageResponse<UserSummary> results = userService.getSummaryList(query);
        assertEquals(2, results.totalElements());
        assertEquals(results.content().get(0).id(), user1.getAzureOid());
        assertEquals(results.content().get(0).name(), user1.getName());
        assertEquals(results.content().get(1).id(), user2.getAzureOid());
        assertEquals(results.content().get(1).name(), user2.getName());
    }

    @Test
    void testGetSummaryList_FilterByJob() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setJob("Developer");
        user2.setJob("Developer");
        user3.setJob("Guy");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        PageQuery query = new PageQuery(0, 10, "dev", "job");

        PageResponse<UserSummary> results = userService.getSummaryList(query);
        assertEquals(2, results.totalElements());
        assertEquals(results.content().get(0).id(), user1.getAzureOid());
        assertEquals(results.content().get(1).id(), user2.getAzureOid());
    }

    @Test
    void testGetProjectParticipants_FilterByName() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setName("Jane Developer");
        user2.setName("Janice Developer");
        user3.setName("John Developer");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Project project1 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = sprintRepository.save(TestEntityFactory.createBaseSprint(project1));
        Sprint sprint2 = sprintRepository.save(TestEntityFactory.createBaseSprint(project1));
        Ticket ticket1 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        Ticket ticket2 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user2));

        Project project2 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint3 = sprintRepository.save(TestEntityFactory.createBaseSprint(project2));
        Ticket ticket3 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint3, user3));

        PageQuery query = new PageQuery(0, 10, "jan", "name");

        PageResponse<UserSummary> participants = userService.getProjectParticipants(
                project1.getId(),
                query
        );

        assertEquals(2, participants.totalElements());
        assertEquals(participants.content().get(0).id(), user1.getAzureOid());
        assertEquals(participants.content().get(0).name(), user1.getName());
        assertEquals(participants.content().get(1).id(), user2.getAzureOid());
        assertEquals(participants.content().get(1).name(), user2.getName());
    }

    @Test
    void testGetProjectParticipants_FilterByJob() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setJob("Developer");
        user2.setJob("Developer");
        user3.setJob("Guy");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Project project1 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = sprintRepository.save(TestEntityFactory.createBaseSprint(project1));
        Sprint sprint2 = sprintRepository.save(TestEntityFactory.createBaseSprint(project1));
        Ticket ticket1 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        Ticket ticket2 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user2));

        Project project2 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint3 = sprintRepository.save(TestEntityFactory.createBaseSprint(project2));
        Ticket ticket3 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint3, user3));

        PageQuery query = new PageQuery(0, 10, "dev", "job");

        PageResponse<UserSummary> participants = userService.getProjectParticipants(
                project1.getId(),
                query
        );

        assertEquals(2, participants.totalElements());
        assertEquals(participants.content().get(0).id(), user1.getAzureOid());
        assertEquals(participants.content().get(1).id(), user2.getAzureOid());
    }

    @Test
    void testGetSprintParticipants_FilterByName() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setName("Jane Developer");
        user2.setName("Janice Developer");
        user3.setName("John Developer");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        Sprint sprint2 = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        Ticket ticket1 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        Ticket ticket2 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user2));
        Ticket ticket3 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user3));

        PageQuery query = new PageQuery(0, 10, "jan", "name");

        PageResponse<UserSummary> participants = userService.getSprintParticipants(
                sprint1.getId(),
                query
        );

        assertEquals(2, participants.totalElements());
        assertEquals(participants.content().get(0).id(), user1.getAzureOid());
        assertEquals(participants.content().get(0).name(), user1.getName());
        assertEquals(participants.content().get(1).id(), user2.getAzureOid());
        assertEquals(participants.content().get(1).name(), user2.getName());
    }

    @Test
    void testGetSprintParticipants_FilterByJob() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        User user3 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");
        user3.setAzureOid("ghi789");
        user1.setJob("Developer");
        user2.setJob("Developer");
        user3.setJob("Guy");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint1 = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        Sprint sprint2 = sprintRepository.save(TestEntityFactory.createBaseSprint(project));
        Ticket ticket1 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        Ticket ticket2 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user2));
        Ticket ticket3 = ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user3));

        PageQuery query = new PageQuery(0, 10, "dev", "job");

        PageResponse<UserSummary> participants = userService.getSprintParticipants(
                sprint1.getId(),
                query
        );

        assertEquals(2, participants.totalElements());
        assertEquals(participants.content().get(0).id(), user1.getAzureOid());
        assertEquals(participants.content().get(1).id(), user2.getAzureOid());
    }

    @Test
    void testCreateUser() {
        User toCreate = TestEntityFactory.createBaseUser();
        userService.createUser(toCreate);

        User created = userRepository.findById(toCreate.getAzureOid()).orElseThrow();
        assertEquals(created.getAzureOid(), toCreate.getAzureOid());
        assertEquals(created.getName(), toCreate.getName());
    }

    @Test
    void testUpdateUser() {
        User toUpdate = userRepository.save(TestEntityFactory.createBaseUser());
        toUpdate.setName("Jane Developer");

        userService.updateUser(toUpdate);

        User updated = userRepository.findById(toUpdate.getAzureOid()).orElseThrow();
        assertEquals(updated.getAzureOid(), toUpdate.getAzureOid());
        assertEquals(updated.getName(), toUpdate.getName());
    }

    @Test
    void testDeleteUserById() {
        User existing = userRepository.save(TestEntityFactory.createBaseUser());
        userService.deleteUserById(existing.getAzureOid());
        assertFalse(userRepository.existsById(existing.getAzureOid()));
    }
}
