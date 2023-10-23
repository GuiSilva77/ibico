package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Oportunity;
import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OportunityDto;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.entities.payload.OportunityPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.OportunityRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.OportunityService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OportunityServiceImpl implements OportunityService {

    private final OportunityRepository oportunityRepository;

    private final UserRepository userRepository;

    private final EntityManager entityManager;

    public OportunityServiceImpl(OportunityRepository oportunityRepository, UserRepository userRepository, EntityManager entityManager) {
        this.oportunityRepository = oportunityRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Response<OportunityDto> findOportunities(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Oportunity> result = searchSession.search(Oportunity.class)
                .where(f -> f.wildcard().fields("title", "description").matching(query + "*"))
                .fetch(pageNo * pageSize, pageSize);

        List<OportunityDto> oportunities = result.hits().stream()
                .map(Oportunity::toOportunityDto)
                .toList();

        long totalElements = result.total().hitCount();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        boolean last = (pageNo + 1) >= totalPages;

        return new Response<>(oportunities, pageNo, pageSize, (int) totalElements, totalPages, last, false);
    }

    @Override
    public OportunityDto findOportunityById(String Id) {
        return oportunityRepository.findById(UUID.fromString(Id))
                .orElseThrow(() -> new ResourceNotFoundException("Oportunity", "Id", Id))
                .toOportunityDto();
    }

    @Override
    public OportunityDto saveOportunity(OportunityPayload payload, String cpf) {
        Oportunity oportunity = payload.toOportunity();

        oportunity.setPostedBy(userRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cpf", "")));

        return oportunityRepository.save(oportunity).toOportunityDto();
    }

    @Override
    public OportunityDto updateOportunity(OportunityPayload oportunityDto, String id) {
        Oportunity oportunity = oportunityRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Oportunity", "Id", id));

        oportunity.setTitle(oportunityDto.title());
        oportunity.setDescription(oportunityDto.description());
        oportunity.setStartDateTime(oportunityDto.startDateTime());
        oportunity.setEndDateTime(oportunityDto.endDateTime());
        oportunity.setTimeLoad(oportunityDto.timeLoad());
        oportunity.setLocal(oportunityDto.local());
        oportunity.setValue(oportunityDto.value());
        oportunity.setNecessarySkills(oportunityDto.necessarySkills().stream().map(SkillDto::toSkill).collect(Collectors.toSet()));
        oportunity.setStatus(oportunityDto.status());

        return oportunityRepository.save(oportunity).toOportunityDto();
    }

    @Override
    public void deactivateOportunity(String id) {
        Oportunity oportunity = oportunityRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Oportunity", "Id", id));

        oportunityRepository.delete(oportunity);
    }
}
