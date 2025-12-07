package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ProjectMapperTest {

    @Autowired
    private ProjectMapper mapper;

    @Test
    void testToSummaryList() {
        Project project = new Project(
                10L,
                "Project 1",
                "Description 1",
                "p1.png",
                ProjectStatus.ACTIVE,
                null
        );

        ProjectSummary summary = mapper.toSummary(project);

        assertEquals(10L, summary.id());
        assertEquals("Project 1", summary.title());
    }

    @Test
    void testCreateToEntity() {
        ProjectCreate command = new ProjectCreate("Project", "Description", "pp.png", ProjectStatus.ACTIVE);

        Project project = mapper.createToEntity(command);

        assertEquals("Project", project.getTitle());
        assertNull(project.getId());
    }

    @Test
    void testUpdateEntity() {
        Project old = new Project(
                1L, "Old Title", "Old Description", "pic2.png",
                ProjectStatus.ACTIVE, null
        );
        ProjectUpdate command = new ProjectUpdate(
                1L, "New Title", "New Description", null, ProjectStatus.DONE
        );

        Project updated = mapper.updateEntity(command, old);

        assertEquals("New Title", updated.getTitle());
        assertEquals("pic2.png", updated.getProfilePicture());
    }
}
