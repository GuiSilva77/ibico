package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.PasswordResetCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link PasswordResetCode}
 */
public record PasswordResetCodeDto(UUID requestId, String code, LocalDateTime expiresAt) implements Serializable {
}