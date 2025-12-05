package com.ilemgroup.internship.taskmanager.backend.repository;

import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<@NonNull Sprint, @NonNull Long> {
}
