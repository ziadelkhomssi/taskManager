package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(MapperTestConfiguration.class)
class TicketCommentMapperTest{

    @Autowired
    private TicketCommentMapper mapper;



    @Test
    void testToDetails() {
        User user = new User(
                "123", "Bob", "Dev", "pic",
                null, null, null
        );

        TicketComment parentComment = new TicketComment(
                1L, user, "Hello!", null, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        TicketComment comment = new TicketComment(
                2L, user, "Hello Also!", null, parentComment, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        TicketCommentDetails details = mapper.toDetails(comment);

        assertEquals(2L, details.id());
        assertEquals("Hello Also!", details.comment());
        assertEquals(1L, details.parentCommentId());
    }
}