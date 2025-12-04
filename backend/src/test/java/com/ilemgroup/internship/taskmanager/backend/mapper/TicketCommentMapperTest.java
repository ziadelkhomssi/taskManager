package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
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
class TicketCommentMapperTest{

    @Autowired
    private TicketCommentMapper mapper;

    @Test
    void testToDetails() {
        User user = new User("123", "Bob", "Dev", "pic", null, null, null);

        TicketComment parent = new TicketComment();
        parent.setId(10L);

        TicketComment comment = new TicketComment(
                1L, user, "Hello!", null, parent, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        TicketCommentDetails dto = mapper.toDetails(comment);

        assertEquals(1L, dto.id());
        assertEquals("Hello!", dto.comment());
        assertEquals(10L, dto.parentCommentId());

        UserSummary userDto = dto.userSummary();
        assertEquals("123", userDto.id());
    }
}