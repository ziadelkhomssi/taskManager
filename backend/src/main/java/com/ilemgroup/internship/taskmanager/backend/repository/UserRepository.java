package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull String> {
    Page<@NonNull User> findAll(Specification<@NonNull User> specification, Pageable pageable);
}
