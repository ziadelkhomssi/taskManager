package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    @Query("""
            SELECT DISTINCT tc FROM TicketComment tc
            LEFT JOIN Ticket ti ON tc.ticket.id=ti.id
            WHERE ti.id=?1
    """)
    Page<TicketComment> findAllByTicketId(Long ticketId, Pageable pageable);
}
