package com.ilemgroup.internship.taskmanager.backend.entity;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "sprints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private Instant startDate;
    private Instant dueDate;
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    private SprintStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}

