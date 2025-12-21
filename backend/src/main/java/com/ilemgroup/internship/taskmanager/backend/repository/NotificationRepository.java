package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@SuppressWarnings("NullableProblems")
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("""
            SELECT DISTINCT no FROM Notification no
            LEFT JOIN Ticket ti ON no.ticket.id=ti.id
            LEFT JOIN User us ON ti.user.azureOid=us.azureOid
            WHERE us.azureOid=?1
    """)
    Page<Notification> findAllByUserId(String userId, Pageable pageable);
}
