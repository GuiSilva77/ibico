package br.com.ibico.api.entities.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link br.com.ibico.api.entities.User}
 */
public record UserGetDto(@NotNull String name, @NotNull String username, @NotNull LocalDateTime dateOfCreation,
                         @NotNull String imgURL, boolean active, @NotNull String telephone, Set<SkillDto> skills,
                         Set<OpportunityDto> opportunitiesPosted, Set<ReviewDto> reviews) implements Serializable {
}