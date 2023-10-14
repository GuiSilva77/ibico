package br.com.ibico.api.services;

import br.com.ibico.api.entities.dto.SkillDto;

public interface SkillService {
    SkillDto findSkillById(String id);
    SkillDto saveSkill(SkillDto payload);
    SkillDto updateSkill(String id, SkillDto SkillDto);
    void deactivateSkill(String id);
}
