package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.TestEntityFactory;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TicketCommentMapperTest{

    @Autowired
    private TicketCommentMapper mapper;

    @Test
    void testToDetails() {
        User user1 = TestEntityFactory.createBaseUser();
        User user2 = TestEntityFactory.createBaseUser();
        user1.setAzureOid("abc123");
        user2.setAzureOid("def456");

        Ticket ticket = TestEntityFactory.createBaseTicket(null, null);
        TicketComment parentComment = TestEntityFactory.createBaseTicketComment(ticket, user1);
        TicketComment comment = TestEntityFactory.createBaseTicketComment(ticket, user2);
        parentComment.setId(1L);
        comment.setId(2L);
        parentComment.setComment("Hello!");
        comment.setComment("Hello Also!");
        comment.setParentComment(parentComment);

        TicketCommentDetails details = mapper.toDetails(comment);

        assertEquals(comment.getId(), details.id());
        assertEquals(comment.getComment(), details.comment());
        assertEquals(comment.getParentComment().getId(), details.parentCommentId());
    }

    @Test
    void testCreateToEntity() {
        TicketCommentCreate command = new TicketCommentCreate(
                "Test comment",
                20L,
                30L
        );

        TicketComment entity = mapper.createToEntity(command);

        assertNull(entity.getId());
        assertEquals(command.comment(), entity.getComment());

        assertNull(entity.getUser());
        assertNull(entity.getTicket());
        assertNull(entity.getParentComment());

        assertNull(entity.getReplies());

        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void testUpdateEntity() {
        TicketComment old = TestEntityFactory.createBaseTicketComment(null, null);
        old.setId(5L);

        TicketCommentUpdate command = new TicketCommentUpdate(
                5L,
                "Updated comment"
        );

        TicketComment updated = mapper.updateEntity(command, old);

        assertEquals(old.getId(), updated.getId());
        assertEquals(command.comment(), updated.getComment());

        assertNull(updated.getUser());
        assertNull(updated.getTicket());
        assertNull(updated.getParentComment());
        assertNull(updated.getReplies());

        assertNull(updated.getCreatedAt());
        assertNull(updated.getUpdatedAt());
    }
}