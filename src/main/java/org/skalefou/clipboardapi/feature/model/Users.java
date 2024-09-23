package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// This class represents the model of the 'users' table, thus storing user information
@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "mail", unique = true, updatable = false, columnDefinition = "VARCHAR(255)")
    private String mail;

    @Column(name = "password")
    private String password;

    @Column(name = "oauth2_id", unique = true, nullable = true, updatable = false,  columnDefinition = "VARCHAR(255)")
    private String oauth2Id;

    @Column(name = "oauth2_provider", nullable = true, updatable = false, columnDefinition = "VARCHAR(16)")
    private String oauth2Provider;

    @CreationTimestamp
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();
}
