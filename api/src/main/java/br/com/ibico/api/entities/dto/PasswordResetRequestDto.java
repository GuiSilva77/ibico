package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.PasswordResetRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link PasswordResetRequest}
 */
public record PasswordResetRequestDto(String accessToken, LocalDateTime expirationDate) implements Serializable {
}