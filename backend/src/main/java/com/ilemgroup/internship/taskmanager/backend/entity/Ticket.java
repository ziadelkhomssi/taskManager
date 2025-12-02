package com.ilemgroup.internship.taskmanager.backend.entity;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Ticket priority;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime closedAt;

    @ManyToOne
    @JoinColumn(name = "closed_by")
    private User closedBy;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketComment> comments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Notification> notifications;
}

