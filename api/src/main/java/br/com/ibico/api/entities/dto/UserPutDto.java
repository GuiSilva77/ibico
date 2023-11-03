package br.com.ibico.api.entities.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record UserPutDto(@NotNull String name, @NotNull String username, @NotNull LocalDateTime dateOfCreation,
                         @NotNull String imgURL, boolean active, @NotNull String telephone,
                         Set<SkillDto> skills) implements Serializable {

}
