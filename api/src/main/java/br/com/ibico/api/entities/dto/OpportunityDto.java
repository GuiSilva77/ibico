package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.Opportunity;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.enums.OpportunityStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link Opportunity}
 */
public record OpportunityDto(UUID id, @NotNull String title, @NotNull String description,
                             @NotNull LocalDateTime startDateTime, @NotNull LocalDateTime endDateTime,
                             @NotNull String timeLoad, @NotNull String local, @NotNull BigDecimal value,
                             @NotNull Set<SkillDto> necessarySkills, @NotNull OpportunityStatus status,
                             @NotNull LocalDateTime createdAt, UserDto postedBy,
                             LocalDateTime opportunityClosedTIme) implements Serializable {
    /**
     * DTO for {@link User}
     */
    public record UserDto(@NotNull String name, @NotNull String username,
                          @NotNull String imgURL) implements Serializable {
    }
}