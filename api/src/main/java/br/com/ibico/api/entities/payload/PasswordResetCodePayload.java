package br.com.ibico.api.entities.payload;

import java.io.Serializable;

/**
 * DTO for {@link br.com.ibico.api.entities.PasswordResetCode}
 */
public record PasswordResetCodePayload(String requestId, String userCpf, String code) implements Serializable {
}