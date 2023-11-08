package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.Opportunity;
import br.com.ibico.api.entities.Review;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link br.com.ibico.api.entities.Review}
 */
public record ReviewPayload(@NotNull String review, int rating,
                            String opportunityId) implements Serializable {
    public Review toReview() {
        return new Review(review, rating, new Opportunity(UUID.fromString(opportunityId)));
    }
}