package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("""
            SELECT ti FROM Ticket ti
            WHERE LOWER(ti.title) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Ticket> findAllByTicketName(String search, Pageable pageable);
    @Query("""
            SELECT ti FROM Ticket ti
            WHERE LOWER(ti.status) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Ticket> findAllByTicketStatus(String search, Pageable pageable);
    @Query("""
            SELECT ti FROM Ticket ti
            LEFT JOIN User us ON ti.user.azureOid=us.azureOid
            WHERE LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Ticket> findAllByUserName(String search, Pageable pageable);
}
