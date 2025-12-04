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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class ProjectMapperTest {

    @Autowired
    private ProjectMapper mapper;

    @Test
    void testToSummary() {
        Project entity = new Project(10L, "P1", "Desc", "p.png", ProjectStatus.ACTIVE, null);

        ProjectSummary dto = mapper.toSummary(entity);

        assertEquals(10L, dto.id());
        assertEquals("P1", dto.title());
    }

    @Test
    void testCreateToEntity() {
        ProjectCreate dto = new ProjectCreate("Proj", "D", "pp.png", ProjectStatus.ACTIVE);

        Project entity = mapper.createToEntity(dto);

        assertEquals("Proj", entity.getTitle());
        assertNull(entity.getId());
    }

    @Test
    void testUpdateEntity() {
        ProjectUpdate dto = new ProjectUpdate(
                1L, "New Title", "New Desc", "pic2.png", ProjectStatus.DONE
        );

        Project entity = mapper.updateToEntity(dto);

        assertEquals("New Title", entity.getTitle());
    }
}
