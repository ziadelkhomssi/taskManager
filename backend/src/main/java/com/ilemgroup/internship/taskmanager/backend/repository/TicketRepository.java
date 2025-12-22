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
            WHERE ti.sprint.id = ?1
            """)
    Page<Ticket> findAllBySprintId(Long sprintId, Pageable pageable);
    @Query("""
            SELECT DISTINCT ti FROM Ticket ti
            LEFT JOIN Sprint sp ON sp.id=ti.sprint.id
            LEFT JOIN User us ON ti.user.azureOid=us.azureOid
            WHERE
            sp.id = ?1 AND
            ((?3 = 'TICKET' AND LOWER(ti.title) LIKE LOWER(CONCAT('%', ?2, '%')))
            OR (?3 = 'STATUS' AND LOWER(ti.status) LIKE LOWER(CONCAT('%', ?2, '%')))
            OR (?3 = 'USER' AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?2, '%'))))
    """)
    Page<Ticket> findAllWithFilter(Long sprintId, String search, String filter, Pageable pageable);
}
