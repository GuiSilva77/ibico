package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link br.com.ibico.api.entities.User}
 */
public record UserPayload(
        @NotNull(message = "CPF must not be null") @Size(message = "CPF must contain 11 characters", min = 11, max = 11) String cpf,
        @NotNull(message = "Name must not be null") String name,
        @NotNull(message = "Username must not be null") String username,
        @NotNull(message = "Password must not be null") @Size(message = "Password must contain at least 8 characters", min = 8) String passwd,
        @NotNull(message = "Image URL must not be null") String imgURL, boolean active,
        @NotNull(message = "Telephone must not be null") String telephone,
        @Size(message = "User must contain at least 1 skill", min = 1) Set<SkillsPayload> skills) implements Serializable {
    public User toUser() {
        return new User(cpf, name, username, passwd, imgURL, active, telephone, skills.stream().map(SkillsPayload::toSkill).collect(Collectors.toSet()));
    }
}

