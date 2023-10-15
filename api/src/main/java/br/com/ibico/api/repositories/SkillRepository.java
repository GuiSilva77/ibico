package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Page<Skill> findByName(String name, Pageable pageable);

    Optional<Skill> findByName(String name);

    Boolean existsByName(String name);
}
