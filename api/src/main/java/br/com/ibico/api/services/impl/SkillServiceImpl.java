package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.SkillRepository;
import br.com.ibico.api.services.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }


    @Override
    public Response<SkillDto> findSkills(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Skill> page = skillRepository.findByName(query, PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortDir), sortBy)));

        if (page.isEmpty()) {
            return new Response<SkillDto>(
                    null,
                    page.getNumber(),
                    page.getSize(),
                    page.getNumberOfElements(),
                    page.getTotalPages(),
                    page.isLast()
            );
        }

        return new Response<SkillDto>(
                page.getContent().stream()
                        .map(Skill::toSkillDto)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public SkillDto addSkill(SkillDto skillDto) {
        Skill skill = skillDto.toSkill();

        return skillRepository.save(skill).toSkillDto();
    }

    @Override
    public SkillDto updateSkill(SkillDto skillDto) {
        Skill skill = skillRepository.findByName(skillDto.name()).orElseThrow(() ->
                new ResourceNotFoundException("Skill", "Name", skillDto.name()));

        skill.setName(skillDto.name());
        skill.setDescription(skillDto.description());

        return skillRepository.save(skill).toSkillDto();
    }
}
