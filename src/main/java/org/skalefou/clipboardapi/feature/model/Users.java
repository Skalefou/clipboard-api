package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

// This class represents the model of the 'users' table, thus storing user information
@Entity
@Data
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "mail", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String mail;

    @Column(name = "password", columnDefinition = "VARCHAR(64)")
    private ZonedDateTime password;

    @Column(name = "registration_date", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private ZonedDateTime registrationDate;

    @Column(name = "last_connection", columnDefinition = "TIMESTAMP", nullable = false)
    private ZonedDateTime lastConnection;
}
