package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<@NonNull Ticket, @NonNull Long> {
    Page<@NonNull Ticket> findAll(Specification<@NonNull Ticket> specification, Pageable pageable);
}
