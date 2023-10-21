package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.SkillRepository;
import br.com.ibico.api.services.SkillService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    private final EntityManager entityManager;

    public SkillServiceImpl(SkillRepository skillRepository, EntityManager entityManager) {
        this.skillRepository = skillRepository;
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public Response<SkillDto> findSkills(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Skill> result = searchSession.search(Skill.class)
                .where(f -> f.wildcard().fields("name", "skillname").matching(query + "*"))
                .fetch(pageNo * pageSize, pageSize);

        List<SkillDto> skills = result.hits().stream()
                .map(Skill::toSkillDto)
                .toList();

        long totalElements = result.total().hitCount();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        boolean last = (pageNo + 1) >= totalPages;

        return new Response<>(skills, pageNo, pageSize, (int) totalElements, totalPages, last, false);
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
