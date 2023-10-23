package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.SkillDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.UUID;

@Entity
@Indexed
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
    @FullTextField
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
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

    public SkillDto toSkillDto() {
        return new SkillDto(this.name);
    }
}
