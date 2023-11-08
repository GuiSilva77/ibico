package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.Opportunity;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.entities.enums.OpportunityStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link Opportunity}
 */
public record OpportunityPayload(@NotNull(message = "Title must not be null") String title,
                                 @NotNull(message = "Description must not be null") String description,
                                 @NotNull(message = "Start date must not be null") @FutureOrPresent(message = "Start date must not be on the past") LocalDateTime startDateTime,
                                 @NotNull(message = "End date must not be null") @FutureOrPresent(message = "End date must not be on the past") LocalDateTime endDateTime,
                                 @NotNull(message = "Time load must not be null") String timeLoad,
                                 @NotNull(message = "Local must not be null") String local,
                                 @NotNull(message = "Value must not be null") @Positive(message = "Value must be positive") BigDecimal value,
                                 @NotNull(message = "Opportunity must have at least one skill")Set<SkillDto>necessarySkills,
                                 @NotNull(message = "Status must not be null") String status) implements Serializable {
    public Opportunity toOpportunity() {
        return new Opportunity(title, description, startDateTime, endDateTime, timeLoad, local, value, necessarySkills.stream().map(SkillDto::toSkill).collect(Collectors.toSet()), OpportunityStatus.valueOf(status));
    }
}