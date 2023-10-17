package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.Oportunity;
import br.com.ibico.api.entities.enums.OportunityStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link br.com.ibico.api.entities.Oportunity}
 */
public record OportunityPayload(@NotNull(message = "Title must not be null") String title,
                                @NotNull(message = "Description must not be null") String description,
                                @NotNull(message = "Start date must not be null") @FutureOrPresent(message = "Start date must not be on the past") LocalDateTime startDateTime,
                                @NotNull(message = "End date must not be null") @FutureOrPresent(message = "End date must not be on the past") LocalDateTime endDateTime,
                                @NotNull(message = "Time load must not be null") String timeLoad,
                                @NotNull(message = "Local must not be null") String local,
                                @NotNull(message = "Value must not be null") @Positive(message = "Value must be positive") BigDecimal value,
                                @NotNull(message = "Occupation must not be null") String occupation,
                                @NotNull(message = "Status must not be null") OportunityStatus status) implements Serializable {
    public Oportunity toOportunity() {
        return new Oportunity(title, description, startDateTime, endDateTime, timeLoad, local, value, occupation, status);
    }
}