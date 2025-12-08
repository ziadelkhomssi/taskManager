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
                "abc123", "John Doe", "johndo@email.com",
                "Engineer", "picture.png",
                null, null
        );

        Ticket ticket = new Ticket(
                99L, "Fix login", "Auth issue", null, user,
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS,
                LocalDateTime.now(), null, null, null
        );

        TicketDetails details = mapper.toDetails(ticket);

        assertEquals(99L, details.id());
        assertEquals("Fix login", details.title());
        assertEquals("Auth issue", details.description());
        assertEquals("Alice", details.userSummary().name());
    }

    @Test
    void testCreateEntity() {
        TicketCreate command = new TicketCreate(
                "New Ticket", "Description", "abc123",
                TicketPriority.LOW, TicketStatus.IN_PROGRESS,
                1L
        );

        Ticket entity = mapper.createToEntity(command);

        assertNull(entity.getId());
        assertEquals("New Ticket", entity.getTitle());
        assertEquals("Description", entity.getDescription());
        assertEquals(TicketPriority.LOW, entity.getPriority());
    }

    @Test
    void testUpdateToEntity() {
        Ticket old = new Ticket(
                99L, "Old Title", "New Description", null, null,
                TicketPriority.MEDIUM, TicketStatus.IN_PROGRESS,
                LocalDateTime.now(), null, null, null
        );
        TicketUpdate command = new TicketUpdate(
                1L, "New Title", "New Description",
                null, null, TicketStatus.COMPLETED
        );

        Ticket entity = mapper.updateEntity(command, old);

        assertEquals("New Title", entity.getTitle());
        assertEquals("New Description", entity.getDescription());
        assertEquals(TicketPriority.MEDIUM, entity.getPriority());
        assertEquals(TicketStatus.COMPLETED, entity.getStatus());
    }
}
