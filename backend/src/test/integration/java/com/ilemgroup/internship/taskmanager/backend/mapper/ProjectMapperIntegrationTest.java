package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ProjectMapperIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    private ProjectMapper mapper;

    @Test
    void testToSummary() {
        Project project = TestEntityFactory.createBaseProject();

        ProjectSummary summary = mapper.toSummary(project, BASE_URL);

        assertEquals(project.getId(), summary.id());
        assertEquals(project.getTitle(), summary.title());
        assertEquals(BASE_URL + "/project/profile-picture/" + project.getId(), summary.profilePictureUrl());
    }

    @Test
    void testToDetails() {
        Project project = TestEntityFactory.createBaseProject();

        ProjectDetails details = mapper.toDetails(project, BASE_URL);

        assertEquals(project.getId(), details.id());
        assertEquals(project.getTitle(), details.title());
        assertEquals(BASE_URL + "/project/profile-picture/" + project.getId(), details.profilePictureUrl());
    }

    @Test
    void testCreateToEntity() {
        ProjectCreate command = new ProjectCreate(
                "Project", "Description",
                ProjectStatus.ACTIVE
        );

        Project project = mapper.createToEntity(command);

        assertEquals(command.title(), project.getTitle());
        assertNull(project.getId());
    }

    @Test
    void testUpdateEntity() {
        Project old = TestEntityFactory.createBaseProject();
        old.setProfilePicturePath("pic2.png");

        ProjectUpdate command = new ProjectUpdate(
                1L, "New Title", "New Description", ProjectStatus.DONE
        );

        Project updated = mapper.updateEntity(command, old);

        assertEquals(command.title(), updated.getTitle());
        assertEquals(old.getProfilePicturePath(), updated.getProfilePicturePath());
    }
}
