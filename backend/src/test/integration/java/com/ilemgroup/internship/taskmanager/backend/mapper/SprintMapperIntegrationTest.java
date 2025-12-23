package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SprintMapperIntegrationTest {

    @Autowired
    private SprintMapper mapper;

    @Test
    void testToSummaryList() {
        Sprint sprint = TestEntityFactory.createBaseSprint(null);
        sprint.setId(1L);

        SprintSummary summary = mapper.toSummary(sprint);

        assertEquals(sprint.getId(), summary.id());
        assertEquals(sprint.getTitle(), summary.title());
    }

    @Test
    void testCreateToEntity() {
        SprintCreate command = new SprintCreate(
                "Sprint!", "Description",
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant(),
                null,
                SprintStatus.ACTIVE, 1L
        );

        Sprint sprint = mapper.createToEntity(command);

        assertEquals(command.title(), sprint.getTitle());
        assertNull(sprint.getId());
    }

    @Test
    void testUpdateEntity() {
        Sprint old = TestEntityFactory.createBaseSprint(null);
        old.setStartDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        SprintUpdate command = new SprintUpdate(
                1L, "New Title", "New Description",
                null, null, null, SprintStatus.DONE
        );

        Sprint updated = mapper.updateEntity(command, old);

        assertEquals(command.title(), updated.getTitle());
        assertEquals(old.getStartDate(), updated.getStartDate());
    }
}
