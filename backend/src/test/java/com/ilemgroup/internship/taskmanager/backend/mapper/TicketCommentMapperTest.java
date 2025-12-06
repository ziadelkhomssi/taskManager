package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
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
class TicketCommentMapperTest{

    @Autowired
    private TicketCommentMapper mapper;



    @Test
    void testToDetails() {
        User user = new User(
                "123", "Bob", "Dev", "pic",
                null, null
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

    @Test
    void testCreateToEntity() {
        TicketCommentCreate create = new TicketCommentCreate(
                "Test comment",
                20L,
                30L
        );

        TicketComment entity = mapper.createToEntity(create);

        assertNull(entity.getId());
        assertEquals("Test comment", entity.getComment());

        assertNull(entity.getUser());
        assertNull(entity.getTicket());
        assertNull(entity.getParentComment());

        assertNull(entity.getReplies());

        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void testUpdateToEntity() {
        TicketComment existing = new TicketComment();
        existing.setId(5L);
        existing.setComment("Old comment");

        User user = new User("999", "John", "Dev", "pic", null, null);
        existing.setUser(user);

        TicketCommentUpdate update = new TicketCommentUpdate(
                5L,
                "Updated comment"
        );

        TicketComment updated = mapper.updateToEntity(update);

        assertEquals(5L, updated.getId());
        assertEquals("Updated comment", updated.getComment());

        assertNull(updated.getUser());
        assertNull(updated.getTicket());
        assertNull(updated.getParentComment());
        assertNull(updated.getReplies());

        assertNull(updated.getCreatedAt());
        assertNull(updated.getUpdatedAt());
    }
}