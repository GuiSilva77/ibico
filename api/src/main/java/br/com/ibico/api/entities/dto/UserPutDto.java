package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.User;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record UserPutDto(@NotNull String name, @NotNull String username, @NotNull LocalDateTime dateOfCreation,
                         @NotNull String imgURL, boolean active, @NotNull String telephone,
                         Set<SkillDto> skills) implements Serializable {
    public User toUser() {
        return new User(this.name, this.username, this.dateOfCreation, this.imgURL, this.active, this.telephone, this.skills.stream().map(SkillDto::toSkill).collect(Collectors.toSet()));
    }
}
