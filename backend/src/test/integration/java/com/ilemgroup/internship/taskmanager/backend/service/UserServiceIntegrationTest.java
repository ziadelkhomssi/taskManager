package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> results =
                userService.getAllUsers(pageable, "jan", "name");

        assertEquals(2, results.getTotalElements());
        assertEquals(user1.getAzureOid(), results.getContent().get(0).id());
        assertEquals(user1.getName(), results.getContent().get(0).name());
        assertEquals(user2.getAzureOid(), results.getContent().get(1).id());
        assertEquals(user2.getName(), results.getContent().get(1).name());
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

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> results =
                userService.getAllUsers(pageable, "dev", "job");

        assertEquals(2, results.getTotalElements());
        assertEquals(user1.getAzureOid(), results.getContent().get(0).id());
        assertEquals(user2.getAzureOid(), results.getContent().get(1).id());
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
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user2));

        Project project2 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint3 = sprintRepository.save(TestEntityFactory.createBaseSprint(project2));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint3, user3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> participants =
                userService.getProjectParticipants(
                        project1.getId(),
                        pageable,
                        "jan",
                        "name"
                );

        assertEquals(2, participants.getTotalElements());
        assertEquals(user1.getAzureOid(), participants.getContent().get(0).id());
        assertEquals(user1.getName(), participants.getContent().get(0).name());
        assertEquals(user2.getAzureOid(), participants.getContent().get(1).id());
        assertEquals(user2.getName(), participants.getContent().get(1).name());
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
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user2));

        Project project2 = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint3 = sprintRepository.save(TestEntityFactory.createBaseSprint(project2));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint3, user3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> participants =
                userService.getProjectParticipants(
                        project1.getId(),
                        pageable,
                        "dev",
                        "job"
                );

        assertEquals(2, participants.getTotalElements());
        assertEquals(user1.getAzureOid(), participants.getContent().get(0).id());
        assertEquals(user2.getAzureOid(), participants.getContent().get(1).id());
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
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user2));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> participants =
                userService.getSprintParticipants(
                        sprint1.getId(),
                        pageable,
                        "jan",
                        "name"
                );

        assertEquals(2, participants.getTotalElements());
        assertEquals(user1.getAzureOid(), participants.getContent().get(0).id());
        assertEquals(user1.getName(), participants.getContent().get(0).name());
        assertEquals(user2.getAzureOid(), participants.getContent().get(1).id());
        assertEquals(user2.getName(), participants.getContent().get(1).name());
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
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user1));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint1, user2));
        ticketRepository.save(TestEntityFactory.createBaseTicket(sprint2, user3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummary> participants =
                userService.getSprintParticipants(
                        sprint1.getId(),
                        pageable,
                        "dev",
                        "job"
                );

        assertEquals(2, participants.getTotalElements());
        assertEquals(user1.getAzureOid(), participants.getContent().get(0).id());
        assertEquals(user2.getAzureOid(), participants.getContent().get(1).id());
    }

    @Test
    void testGetSummaryList_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(ResponseStatusException.class, () ->
                userService.getAllUsers(pageable, "x", "unknown_filter")
        );
    }

    @Test
    void testGetProjectParticipants_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());

        assertThrows(ResponseStatusException.class, () ->
                userService.getProjectParticipants(project.getId(), pageable, "x", "unknown_filter")
        );
    }

    @Test
    void testGetSprintParticipants_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Project project = projectRepository.save(TestEntityFactory.createBaseProject());
        Sprint sprint = sprintRepository.save(TestEntityFactory.createBaseSprint(project));

        assertThrows(ResponseStatusException.class, () ->
                userService.getSprintParticipants(sprint.getId(), pageable, "x", "unknown_filter")
        );
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
