package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NotificationMapperTest {

    @Autowired
    private NotificationMapper mapper;

    @Test
    void testToDetails() {
        Ticket ticket1 = new Ticket(
                1L, "Ticket 1", null,
                null, null,
                null, null,
                null, null,
                null, null
        );

        Notification notification = new Notification(
                1L, NotificationType.TICKET_ASSIGNED,
                ticket1, null, false, LocalDateTime.now().minusDays(2)
        );

        NotificationDetails details = mapper.toDetails(notification);

        assertEquals(NotificationType.TICKET_ASSIGNED, details.type());
        assertEquals(1L, details.ticketSummary().id());
    }
}

