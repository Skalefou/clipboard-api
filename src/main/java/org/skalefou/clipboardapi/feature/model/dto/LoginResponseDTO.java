package org.skalefou.clipboardapi.feature.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private UUID id;
    private String mail;
    private LocalDateTime registrationDate;
    private String accessToken;
}