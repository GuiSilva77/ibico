package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link br.com.ibico.api.entities.User}
 */
public record UserDto(@NotNull @Size(max = 11) String cpf, @NotNull String name, @NotNull LocalDateTime dateOfCreation,
                      @NotNull String imgURL, boolean active, @NotNull String telephone,
                      Set<SkillDto> skills) implements Serializable {
    public User toUser() {
        return new User(this.cpf, this.name, this.dateOfCreation, this.imgURL, this.active, this.telephone, this.skills.stream().map(SkillDto::toSkill).collect(Collectors.toSet()));
    }
}