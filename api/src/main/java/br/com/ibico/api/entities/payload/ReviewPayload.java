package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.Oportunity;
import br.com.ibico.api.entities.Review;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link br.com.ibico.api.entities.Review}
 */
public record ReviewPayload(@NotNull String review, int rating,
                            UUID oportunityId) implements Serializable {
    public Review toReview() {
        return new Review(review, rating, new Oportunity(oportunityId));
    }
}