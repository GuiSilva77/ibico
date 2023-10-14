package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.SkillDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(
        name = "skills",
        uniqueConstraints = @UniqueConstraint(
                name = "skills_uq_name",
                columnNames = "name"
        )
)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "description", length = 50, nullable = false)
    private String description;

    public Skill() {
    }

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SkillDto toSkillDto() {
        return new SkillDto(this.name, this.description);
    }
}
