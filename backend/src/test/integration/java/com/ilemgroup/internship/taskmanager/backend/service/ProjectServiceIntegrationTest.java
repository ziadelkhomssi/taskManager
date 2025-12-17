package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
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
class ProjectServiceIntegrationTest {

    @Autowired
    private ProjectService projectService;

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
        Project project = TestEntityFactory.createBaseProject();
        projectRepository.save(project);

        ProjectDetails details = projectService.getDetailsById(project.getId());

        assertNotNull(details);
        assertEquals(project.getTitle(), details.title());
        assertEquals(project.getStatus(), details.status());
    }

    @Test
    void testGetDetails_NotFound() {
        assertThrows(EntityNotFoundException.class,
                () -> projectService.getDetailsById(999L));
    }

    @Test
    void testGetSummaryList_FilterByProject() {
        Project project1 = TestEntityFactory.createBaseProject();
        Project project2 = TestEntityFactory.createBaseProject();
        project1.setTitle("Alpha");
        project2.setTitle("Beta");
        projectRepository.save(project1);
        projectRepository.save(project2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProjectSummary> result =
                projectService.getSummaryList(pageable, "alp", "project");

        assertEquals(1, result.getTotalElements());
        assertEquals(project1.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetSummaryList_FilterByStatus() {
        Project project1 = TestEntityFactory.createBaseProject();
        Project project2 = TestEntityFactory.createBaseProject();
        project1.setStatus(ProjectStatus.ARCHIVED);
        project2.setStatus(ProjectStatus.ACTIVE);
        projectRepository.save(project1);
        projectRepository.save(project2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProjectSummary> result =
                projectService.getSummaryList(pageable, "arc", "status");

        assertEquals(1, result.getTotalElements());
        assertEquals(project1.getStatus(), result.getContent().getFirst().status());
    }

    @Test
    void testGetSummaryList_FilterBySprint() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint = TestEntityFactory.createBaseSprint(project);
        sprint.setTitle("I AM THE ALPHA!!! RAWR");
        sprintRepository.save(sprint);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProjectSummary> result =
                projectService.getSummaryList(pageable, "alpha", "sprint");

        assertEquals(1, result.getTotalElements());
        assertEquals(project.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetSummaryList_FilterByUser() {
        Project project = projectRepository.save(
                TestEntityFactory.createBaseProject()
        );

        Sprint sprint = sprintRepository.save(
                TestEntityFactory.createBaseSprint(project)
        );

        User user = TestEntityFactory.createBaseUser();
        user.setName("John Developer");
        userRepository.save(user);

        ticketRepository.save(
                TestEntityFactory.createBaseTicket(sprint, user)
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProjectSummary> result =
                projectService.getSummaryList(pageable, "john", "user");

        assertEquals(1, result.getTotalElements());
        assertEquals(project.getTitle(), result.getContent().getFirst().title());
    }

    @Test
    void testGetSummaryList_UnknownFilter() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(
                ResponseStatusException.class,
                () -> projectService.getSummaryList(pageable, "x", "unknown_filter")
        );
    }

    @Test
    void testCreateProject() {
        ProjectCreate command = new ProjectCreate(
                "New Project", "desc",
                null,
                ProjectStatus.ACTIVE
        );

        projectService.createProject(command);

        List<Project> all = projectRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(command.title(), all.getFirst().getTitle());
    }

    @Test
    void testUpdateProject() {
        Project old = projectRepository.save(TestEntityFactory.createBaseProject());

        ProjectUpdate command = new ProjectUpdate(
                old.getId(), "Updated Name", "New Description",
                null,
                ProjectStatus.ARCHIVED
        );

        projectService.updateProject(command);

        Project updated = projectRepository.findById(old.getId()).orElseThrow();

        assertEquals(command.title(), updated.getTitle());
        assertEquals(command.description(), updated.getDescription());
        assertEquals(command.status(), updated.getStatus());
    }

    @Test
    void testUpdateProject_NotFound() {
        ProjectUpdate command = new ProjectUpdate(
                999L, "X", "Y",
                null,
                ProjectStatus.ACTIVE
        );

        assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(command));
    }

    @Test
    void testDeleteProject() {
        Project existing = projectRepository.save(TestEntityFactory.createBaseProject());

        projectService.deleteProjectById(existing.getId());

        assertFalse(projectRepository.existsById(existing.getId()));
    }

    @Test
    void testDeleteProject_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> projectService.deleteProjectById(999L));
    }
}
