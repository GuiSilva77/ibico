package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.dto.SkillDto;

import java.util.Set;

public interface SkillService {

    Response<SkillDto> findSkills(String query, int pageNo, int pageSize, String sortBy, String sortDir);

    SkillDto addSkill(SkillDto skillDto);

    SkillDto updateSkill(SkillDto skillDto);

    Set<Skill> convertToSkills(Set<SkillDto> skillDtos);

}
