package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

// This class represents the model of the 'clipboard' table, storing the content and dates linked
@Entity
@Getter
@Setter
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

    @Column(name = "user_id")
    private UUID userId;
}
