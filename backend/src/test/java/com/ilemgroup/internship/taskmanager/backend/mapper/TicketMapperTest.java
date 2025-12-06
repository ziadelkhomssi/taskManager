package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TicketMapperTest {

    @Autowired
    private TicketMapper mapper;

    @Test
    void testToSummary() {
        Ticket ticket = new Ticket(
                1L, "Ticket 1", "Description 1",
                null, null,
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS,
                null, null,
                null, null
        );

        TicketSummary summary = mapper.toSummary(ticket);

        assertEquals(1L, summary.id());
        assertEquals("Ticket 1", summary.title());
        assertEquals(TicketPriority.MEDIUM, summary.priority());
    }

    @Test
    void testToDetails() {
        User user = new User(
                "id123", "Alice", "Dev", "p.png",
                null, null
        );

        Ticket ticket = new Ticket(
                99L, "Fix login", "Auth issue", null, user,
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS,
                LocalDateTime.now(), null, null, null
        );

        TicketDetails dto = mapper.toDetails(ticket);

        assertEquals(99L, dto.id());
        assertEquals("Fix login", dto.title());
        assertEquals("Auth issue", dto.description());
        assertEquals("Alice", dto.userSummary().name());
    }

    @Test
    void testCreateToEntity() {
        TicketCreate dto = new TicketCreate(
                "New Ticket", "Desc", 10L, TicketPriority.LOW, TicketStatus.IN_PROGRESS
        );

        Ticket entity = mapper.createToEntity(dto);

        assertNull(entity.getId());
        assertEquals("New Ticket", entity.getTitle());
        assertEquals("Desc", entity.getDescription());
        assertEquals(TicketPriority.LOW, entity.getPriority());
    }

    @Test
    void testUpdateToEntity() {
        TicketUpdate update = new TicketUpdate(
                1L, "New Title", "New Desc",
                null, TicketPriority.HIGH, TicketStatus.COMPLETED
        );

        Ticket entity = mapper.updateToEntity(update);

        assertEquals("New Title", entity.getTitle());
        assertEquals("New Desc", entity.getDescription());
        assertEquals(TicketPriority.HIGH, entity.getPriority());
        assertEquals(TicketStatus.COMPLETED, entity.getStatus());
    }
}
