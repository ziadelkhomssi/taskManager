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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SprintMapperTest {

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
                LocalDate.now(), LocalDate.now().plusDays(1),
                SprintStatus.ACTIVE, 1L
        );

        Sprint sprint = mapper.createToEntity(command);

        assertEquals(command.title(), sprint.getTitle());
        assertNull(sprint.getId());
    }

    @Test
    void testUpdateEntity() {
        Sprint old = TestEntityFactory.createBaseSprint(null);
        old.setStartDate(LocalDate.now());

        SprintUpdate command = new SprintUpdate(
                1L, "New Title", "New Description",
                null, null, SprintStatus.DONE
        );

        Sprint updated = mapper.updateEntity(command, old);

        assertEquals(command.title(), updated.getTitle());
        assertEquals(LocalDate.now(), updated.getStartDate());
    }
}
