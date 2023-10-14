package br.com.ibico.api.entities.payload;

import br.com.ibico.api.entities.Skill;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Skill}
 */
public record SkillsPayload(
                            @NotNull(message = "Name must not be null") @Size(message = "Skill name must contain at least three characters", min = 3, max = 50) String name,
                            @NotNull(message = "Skill description must not be null") @Size(min = 3, max = 50) String description) implements Serializable {
    public Skill toSkill() {
        return new Skill(name, description);
    }
}