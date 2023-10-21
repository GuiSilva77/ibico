package br.com.ibico.api.entities.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link br.com.ibico.api.entities.Review}
 */
public record ReviewDto(UUID id, @NotNull String review, int rating, @NotNull LocalDateTime createdAt, UUID reviewerId,
                        UUID oportunityId) implements Serializable {
}