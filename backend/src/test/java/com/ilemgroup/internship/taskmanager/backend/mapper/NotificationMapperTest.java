package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NotificationMapperTest {

    @Autowired
    private NotificationMapper mapper;

    @Test
    void testToDetails() {
        Ticket ticket = TestEntityFactory.createBaseTicket(null, null);
        ticket.setId(1L);
        Notification notification = TestEntityFactory.createBaseNotification(ticket);

        NotificationDetails details = mapper.toDetails(notification);

        assertEquals(notification.getType(), details.type());
        assertEquals(notification.getTicket().getId(), details.ticketSummary().id());
    }
}

