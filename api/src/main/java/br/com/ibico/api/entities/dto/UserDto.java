package br.com.ibico.api.entities.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link br.com.ibico.api.entities.User}
 */
@Schema(name = "UserDto", description = "DTO for User")
public record UserDto(@NotNull @Size(max = 11) String cpf, @NotNull String name, @NotNull String username,@NotNull LocalDateTime dateOfCreation,
                      @NotNull String imgURL, boolean active, @NotNull String telephone,
                      Set<SkillDto> skills) implements Serializable {
}

