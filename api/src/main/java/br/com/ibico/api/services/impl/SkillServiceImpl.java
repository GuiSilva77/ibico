package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.SkillRepository;
import br.com.ibico.api.services.SkillService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository SkillRepository;

    public SkillServiceImpl(SkillRepository SkillRepository) {
        this.SkillRepository = SkillRepository;
    }

    @Override
    public SkillDto findSkillById(String id) {
        Skill Skill = SkillRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ResourceNotFoundException("Skill", "ID", "")
        );

        return Skill.toSkillDto();
    }

    @Override
    public SkillDto saveSkill(SkillDto dto) {
        Skill Skill = dto.toSkill();

        Skill savedSkill = SkillRepository.save(Skill);

        return savedSkill.toSkillDto();
    }

    @Override
    public SkillDto updateSkill(String id, SkillDto SkillDto) {
        Skill Skill = SkillRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ResourceNotFoundException("Skill", "ID", "")
        );

        Skill.setName(SkillDto.name());
        Skill.setDescription(SkillDto.description());

        Skill savedSkill = SkillRepository.save(Skill);

        return savedSkill.toSkillDto();
    }

    @Override
    public void deactivateSkill(String id) {
        Skill Skill = SkillRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new ResourceNotFoundException("Skill", "ID", "")
        );

        SkillRepository.delete(Skill);
    }
}
