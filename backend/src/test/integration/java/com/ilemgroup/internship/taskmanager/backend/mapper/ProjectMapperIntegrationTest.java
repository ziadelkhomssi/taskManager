package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ProjectMapperIntegrationTest {

    @Autowired
    private ProjectMapper mapper;

    @Test
    void testToSummaryList() {
        Project project = TestEntityFactory.createBaseProject();

        ProjectSummary summary = mapper.toSummary(project);

        assertEquals(project.getId(), summary.id());
        assertEquals(project.getTitle(), summary.title());
    }

    @Test
    void testCreateToEntity() {
        ProjectCreate command = new ProjectCreate(
                "Project", "Description", "pp.png",
                ProjectStatus.ACTIVE
        );

        Project project = mapper.createToEntity(command);

        assertEquals(command.title(), project.getTitle());
        assertNull(project.getId());
    }

    @Test
    void testUpdateEntity() {
        Project old = TestEntityFactory.createBaseProject();
        old.setProfilePicture("pic2.png");

        ProjectUpdate command = new ProjectUpdate(
                1L, "New Title", "New Description", null, ProjectStatus.DONE
        );

        Project updated = mapper.updateEntity(command, old);

        assertEquals(command.title(), updated.getTitle());
        assertEquals(old.getProfilePicture(), updated.getProfilePicture());
    }
}
