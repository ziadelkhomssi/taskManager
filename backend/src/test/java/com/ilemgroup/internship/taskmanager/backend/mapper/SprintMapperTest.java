package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SprintMapperTest {

    @Autowired
    private SprintMapper mapper;

    @Test
    void testToSummaryList() {
        Sprint sprint = new Sprint(
                1L, "Sprint 1", "Description 1",
                LocalDate.now(), LocalDate.now().plusDays(5), null,
                SprintStatus.ACTIVE,
                null, null
        );

        SprintSummary summary = mapper.toSummary(sprint);

        assertEquals(1L, summary.id());
        assertEquals("Sprint 1", summary.title());
    }

    @Test
    void testCreateToEntity() {
        SprintCreate command = new SprintCreate(
                "Sprint!", "Description",
                LocalDate.now(), LocalDate.now().plusDays(1),
                SprintStatus.ACTIVE, 1L
        );

        Sprint sprint = mapper.createToEntity(command);

        assertEquals("Sprint!", sprint.getTitle());
        assertNull(sprint.getId());
    }

    @Test
    void testUpdateEntity() {
        Sprint old = new Sprint(
                1L, "Old Title", "Old Description",
                LocalDate.now(), LocalDate.now().plusDays(5), null,
                SprintStatus.ACTIVE,
                null, null
        );
        SprintUpdate command = new SprintUpdate(
                1L, "New Title", "New Description",
                null, null, SprintStatus.DONE
        );

        Sprint updated = mapper.updateEntity(command, old);

        assertEquals("New Title", updated.getTitle());
        assertEquals(LocalDate.now(), updated.getStartDate());
    }
}
