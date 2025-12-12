package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    @Query("""
            SELECT sp FROM Sprint sp
            WHERE LOWER(sp.title) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Sprint> findAllBySprintName(String search, Pageable pageable);
    @Query("""
            SELECT sp FROM Sprint sp
            WHERE LOWER(sp.status) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Sprint> findAllBySprintStatus(String search, Pageable pageable);
    @Query("""
            SELECT sp FROM Sprint sp
            LEFT JOIN Ticket ti ON sp.id=ti.sprint.id
            LEFT JOIN User us ON ti.user.id=us.azureOid
            WHERE LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    Page<Sprint> findAllByUserName(String search, Pageable pageable);
}
