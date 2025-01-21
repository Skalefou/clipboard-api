package org.skalefou.clipboardapi.feature.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
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

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "date_creation", updatable = false, columnDefinition = "TIMESTAMPTZ", nullable = false)
    private ZonedDateTime dateCreation;

    @Column(name = "expiration_time", columnDefinition = "TIMESTAMPTZ")
    private ZonedDateTime expirationTime;

    @Column(name = "last_update", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private ZonedDateTime lastUpdate;

    @Column(name = "access", columnDefinition = "CHAR(5)")
    private String access;

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;
}
