package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
            SELECT DISTINCT pr
            FROM Project pr
            LEFT JOIN Sprint sp ON pr.id=sp.project.id
            LEFT JOIN Ticket ti ON sp.id=ti.sprint.id
            LEFT JOIN User us ON ti.user.id=us.azureOid
            WHERE
            (?2 = 'PROJECT' AND LOWER(pr.title) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'STATUS' AND LOWER(pr.status) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'SPRINT' AND LOWER(sp.title) LIKE LOWER(CONCAT('%', ?1, '%')))
            OR (?2 = 'USER' AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%')))
    """)
    Page<Project> findAllWithFilter(String search, String filter, Pageable pageable);
}
