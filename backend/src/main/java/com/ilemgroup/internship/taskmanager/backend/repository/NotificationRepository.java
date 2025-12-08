package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<@NonNull Notification, @NonNull Long> {
    Page<@NonNull Notification> findAll(Specification<@NonNull Notification> specification, Pageable pageable);
}
