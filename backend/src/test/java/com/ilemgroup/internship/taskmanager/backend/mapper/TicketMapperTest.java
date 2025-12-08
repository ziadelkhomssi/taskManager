package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TicketMapperTest {

    @Autowired
    private TicketMapper mapper;

    @Test
    void testToSummary() {
        Ticket ticket = TestEntityFactory.createBaseTicket(null, null);
        ticket.setId(1L);

        TicketSummary summary = mapper.toSummary(ticket);

        assertEquals(ticket.getId(), summary.id());
        assertEquals(ticket.getTitle(), summary.title());
        assertEquals(ticket.getPriority(), summary.priority());
    }

    @Test
    void testToDetails() {
        User user = TestEntityFactory.createBaseUser();
        Ticket ticket = TestEntityFactory.createBaseTicket(null, user);
        ticket.setId(1L);

        TicketDetails details = mapper.toDetails(ticket);

        assertEquals(ticket.getId(), details.id());
        assertEquals(ticket.getTitle(), details.title());
        assertEquals(ticket.getDescription(), details.description());
        assertEquals(ticket.getUser().getName(), details.userSummary().name());
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
        assertEquals(command.title(), entity.getTitle());
        assertEquals(command.description(), entity.getDescription());
        assertEquals(command.priority(), entity.getPriority());
    }

    @Test
    void testUpdateToEntity() {
        Ticket old = TestEntityFactory.createBaseTicket(null, null);
        old.setPriority(TicketPriority.MEDIUM);

        TicketUpdate command = new TicketUpdate(
                1L, "New Title", "New Description",
                null, null, TicketStatus.COMPLETED
        );

        Ticket updated = mapper.updateEntity(command, old);

        assertEquals(command.title(), updated.getTitle());
        assertEquals(command.description(), updated.getDescription());
        assertEquals(command.status(), updated.getStatus());
        assertEquals(old.getPriority(), updated.getPriority());
    }
}
