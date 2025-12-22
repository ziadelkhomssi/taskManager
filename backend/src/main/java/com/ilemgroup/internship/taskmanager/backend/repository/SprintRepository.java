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
            WHERE sp.project.id = ?1
            """)
    Page<Sprint> findAllByProjectId(Long projectId, Pageable pageable);
    @Query("""
            SELECT DISTINCT sp FROM Sprint sp
            LEFT JOIN Project pr ON pr.id=sp.project.id
            LEFT JOIN Ticket ti ON sp.id=ti.sprint.id
            LEFT JOIN User us ON ti.user.id=us.azureOid
            WHERE
            pr.id = ?1 AND
            ((?3 = 'SPRINT' AND LOWER(sp.title) LIKE LOWER(CONCAT('%', ?2, '%')))
            OR (?3 = 'STATUS' AND LOWER(sp.status) LIKE LOWER(CONCAT('%', ?2, '%')))
            OR (?3 = 'TICKET' AND LOWER(ti.title) LIKE LOWER(CONCAT('%', ?2, '%')))
            OR (?3 = 'USER' AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?2, '%'))))
    """)
    Page<Sprint> findAllWithFilter(Long projectId, String search, String filter, Pageable pageable);
}
