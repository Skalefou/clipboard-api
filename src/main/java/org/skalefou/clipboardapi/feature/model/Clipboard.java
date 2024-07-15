package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// This class represents the model of the 'clipboard' table, storing the content and dates linked
@Entity
@Data
@Table(name = "clipboard")
public class Clipboard {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "access")
    private String access;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private Users user;
}
