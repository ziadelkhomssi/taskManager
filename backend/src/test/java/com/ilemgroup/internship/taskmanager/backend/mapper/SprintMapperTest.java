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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class SprintMapperTest {

    @Autowired
    private SprintMapper mapper;

    @Test
    void testToSummary() {
        Sprint entity = new Sprint(1L, "Sprint 1", "Desc",
                LocalDate.now(), LocalDate.now().plusDays(5), null,
                SprintStatus.ACTIVE, null, null);

        SprintSummary dto = mapper.toSummary(entity);

        assertEquals(1L, dto.id());
        assertEquals("Sprint 1", dto.title());
    }

    @Test
    void testCreateToEntity() {
        SprintCreate dto = new SprintCreate(
                "S1", "D", LocalDate.now(), LocalDate.now().plusDays(1), SprintStatus.ACTIVE
        );

        Sprint entity = mapper.createToEntity(dto);

        assertEquals("S1", entity.getTitle());
        assertNull(entity.getId());
    }

    @Test
    void testUpdateToEntity() {
        SprintUpdate dto = new SprintUpdate(
                5L, "New Title", "New Desc",
                LocalDate.now(), LocalDate.now().plusDays(2), SprintStatus.DONE
        );

        Sprint entity = mapper.updateToEntity(dto);

        assertEquals("New Title", entity.getTitle());
    }
}
