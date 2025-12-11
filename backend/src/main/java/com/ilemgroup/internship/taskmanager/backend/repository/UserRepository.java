package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT us FROM User us WHERE LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<User> findAllUserByName(String search, Pageable pageable);
    @Query("SELECT us FROM User us WHERE LOWER(us.job) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<User> findAllUserByJob(String search, Pageable pageable);
    @Query("SELECT us FROM User us LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid LEFT JOIN Sprint sp ON sp.id=ti.sprint.id LEFT JOIN Project pr ON pr.id=sp.project.id WHERE pr.id=?1 AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?2, '%'))")
    Page<User> findAllProjectParticipantsByName(Long projectId, String search, Pageable pageable);
    @Query("SELECT us FROM User us LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid LEFT JOIN Sprint sp ON sp.id=ti.sprint.id LEFT JOIN Project pr ON pr.id=sp.project.id WHERE pr.id=?1 AND LOWER(us.job) LIKE LOWER(CONCAT('%', ?2, '%'))")
    Page<User> findAllProjectParticipantsByJob(Long projectId, String search, Pageable pageable);
    @Query("SELECT us FROM User us LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid LEFT JOIN Sprint sp ON sp.id=ti.sprint.id WHERE sp.id=?1 AND LOWER(us.name) LIKE LOWER(CONCAT('%', ?2, '%'))")
    Page<User> findAllSprintParticipantsByName(Long sprintId, String search, Pageable pageable);
    @Query("SELECT us FROM User us LEFT JOIN Ticket ti ON ti.user.azureOid=us.azureOid LEFT JOIN Sprint sp ON sp.id=ti.sprint.id WHERE sp.id=?1 AND LOWER(us.job) LIKE LOWER(CONCAT('%', ?2, '%'))")
    Page<User> findAllSprintParticipantsByJob(Long sprintId, String search, Pageable pageable);
}
