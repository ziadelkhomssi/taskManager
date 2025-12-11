package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT pr FROM Project pr WHERE LOWER(pr.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Project> findAllByProjectName(String search, Pageable pageable);
    @Query("SELECT pr FROM Project pr WHERE LOWER(pr.status) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Project> findAllByProjectStatus(String search, Pageable pageable);
    @Query("SELECT pr FROM Project pr LEFT JOIN Sprint sp ON pr.id=sp.project.id WHERE LOWER(sp.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Project> findAllBySprintName(String search, Pageable pageable);
    @Query("SELECT pr FROM Project pr LEFT JOIN Sprint sp ON pr.id=sp.project.id LEFT JOIN Ticket ti ON sp.id=ti.sprint.id LEFT JOIN User us ON ti.user.id=us.azureOid WHERE LOWER(us.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Project> findAllByUserName(String search, Pageable pageable);
}
