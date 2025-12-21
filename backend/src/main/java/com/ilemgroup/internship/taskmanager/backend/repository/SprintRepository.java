package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    @Query("""
            SELECT DISTINCT sp FROM Sprint sp
            LEFT JOIN Ticket ti ON sp.id=ti.sprint.id
            LEFT JOIN User us ON ti.user.id=us.azureOid
            WHERE
            (?2 = 'SPRINT' AND LOWER(sp.title) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'STATUS' AND LOWER(sp.status) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'TICKET' AND LOWER(ti.title) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'USER' AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%')))
    """)
    Page<Sprint> findAllWithFilter(String search, String filter, Pageable pageable);
}
