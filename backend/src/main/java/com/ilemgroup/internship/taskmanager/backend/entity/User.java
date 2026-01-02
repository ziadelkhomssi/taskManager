package com.ilemgroup.internship.taskmanager.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String azureOid;

    private String name;

    private String role; // temporarily added until real authentication is added

    @Email
    private String email;

    private String job;
    private String profilePicturePath;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user")
    private List<TicketComment> comments;
}

