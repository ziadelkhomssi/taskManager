package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@SuppressWarnings("NullableProblems")
public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT DISTINCT us FROM User us
            LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid
            LEFT JOIN Sprint sp ON sp.id=ti.sprint.id
            LEFT JOIN Project pr ON pr.id=sp.project.id
            WHERE
            (:projectId is NULL AND :sprintId is NULL AND :filter = 'NAME' AND
                LOWER(us.name) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            OR (:projectId is NULL AND :sprintId is NULL AND :filter = 'JOB' AND
                LOWER(us.job) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            OR (pr.id = :projectId AND :sprintId is NULL AND :filter = 'NAME' AND
                LOWER(us.name) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            OR (pr.id = :projectId AND :sprintId is NULL AND :filter = 'JOB' AND
                LOWER(us.job) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            OR (:projectId is NULL AND sp.id = :sprintId AND :filter = 'NAME' AND
                LOWER(us.name) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            OR (:projectId is NULL AND sp.id = :sprintId AND :filter = 'JOB' AND
                LOWER(us.job) LIKE LOWER(CONCAT('%', :search, '%'))
            )
    """)
    Page<User> findAllWithFilter(
            @Param("search") String search,
            @Param("filter") String filter,
            @Param("projectId") Long projectId,
            @Param("sprintId") Long sprintId,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT us FROM User us
            LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid
            LEFT JOIN Sprint sp ON sp.id=ti.sprint.id
            LEFT JOIN Project pr ON pr.id=sp.project.id
            WHERE pr.id = ?1
    """)
    Page<User> findAllInProject(Long projectId, Pageable pageable);
    @Query("""
            SELECT DISTINCT us FROM User us
            LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid
            LEFT JOIN Sprint sp ON sp.id=ti.sprint.id
            WHERE sp.id = ?1
    """)
    Page<User> findAllInSprint(Long sprintId, Pageable pageable);
}
