package br.com.ibico.api.entities.payload;

import jakarta.validation.constraints.NotNull;

public record PasswordResetPayload(@NotNull String accessToken, @NotNull String newPassword) {
}
