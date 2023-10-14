package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.Skill;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Skill}
 */
public record SkillDto(@NotNull @Size(min = 3, max = 50) String name,
                       @NotNull @Size(min = 3, max = 50) String description) implements Serializable {
    public Skill toSkill() {
        return new Skill(name, description);
    }
}