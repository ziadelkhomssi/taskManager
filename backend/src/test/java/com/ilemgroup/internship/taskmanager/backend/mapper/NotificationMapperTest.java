package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class NotificationMapperTest {

    @Autowired
    private NotificationMapper mapper;

    @Test
    void testToDetails() {
        Ticket ticket = new Ticket();
        ticket.setId(5L);
        ticket.setTitle("Bug");

        Notification entity = new Notification(
                1L, NotificationType.TICKET_ASSIGNED,
                ticket, null, false, LocalDateTime.now()
        );

        NotificationDetails dto = mapper.toDetails(entity);

        assertEquals(NotificationType.TICKET_ASSIGNED, dto.type());
        assertEquals(5L, dto.ticketSummary().id());
    }
}

