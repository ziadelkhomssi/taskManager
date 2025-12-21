package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("""
            SELECT DISTINCT ti FROM Ticket ti
            LEFT JOIN User us ON ti.user.azureOid=us.azureOid
            WHERE
            (?2 = 'TICKET' AND LOWER(ti.title) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'STATUS' AND LOWER(ti.status) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'USER' AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%')))
    """)
    Page<Ticket> findAllWithFilter(String search, String filter, Pageable pageable);
}
