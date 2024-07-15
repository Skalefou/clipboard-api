package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// This class represents the model of the 'users' table, thus storing user information
@Entity
@Data
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private LocalDateTime password;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "last_connection")
    private LocalDateTime lastConnection;
}
