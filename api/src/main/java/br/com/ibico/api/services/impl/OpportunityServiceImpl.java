package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Opportunity;
import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OpportunityDto;
import br.com.ibico.api.entities.enums.OpportunityStatus;
import br.com.ibico.api.entities.payload.OpportunityPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.exceptions.UnauthorizedException;
import br.com.ibico.api.repositories.OpportunityRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.OpportunityService;
import br.com.ibico.api.services.SkillService;
import jakarta.persistence.EntityManager;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;

    private final UserRepository userRepository;

    private final EntityManager entityManager;
    private final SkillService skillService;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository, UserRepository userRepository, EntityManager entityManager,
                                  SkillService skillService) {
        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.skillService = skillService;
    }

    @Override
    public Response<OpportunityDto> findOwnOpportunities(String cpf) {
        List<OpportunityDto> opportunities = opportunityRepository.findByPostedBy_Cpf(cpf).stream()
                .map(Opportunity::toOpportunityDto)
                .toList();

        int totalElements = opportunities.size();

        return new Response<>(opportunities, 0, 0, totalElements, 0, false, true);
    }

    @Override
    @Transactional
    public Response<OpportunityDto> findOpportunities(String query, int pageNo, int pageSize, String sortBy, String sortDir) {

        Comparator<OpportunityDto> comparator = Comparator.comparing(
                opportunity -> {
                    try {
                        Field field = OpportunityDto.class.getDeclaredField(sortBy);
                        field.setAccessible(true);
                        return (Comparable) field.get(opportunity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        if ("DESC".equals(sortDir)) {
            comparator = comparator.reversed();
        }

        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Opportunity> result = searchSession.search(Opportunity.class)
                .where(f -> f.wildcard().fields("title", "description").matching(query + "*"))
                .fetch(pageNo * pageSize, pageSize);

        List<OpportunityDto> opportunities = result.hits().stream()
                .map(Opportunity::toOpportunityDto)
                .filter(opportunityDto -> opportunityDto.status() != OpportunityStatus.CANCELED)
                .sorted(comparator)
                .toList();

        long totalElements = result.total().hitCount();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        boolean last = (pageNo + 1) >= totalPages;

        return new Response<>(opportunities, pageNo, pageSize, (int) totalElements, totalPages, last, false);
    }

    @Override
    public OpportunityDto findOpportunityById(String Id) {
        return opportunityRepository.findById(UUID.fromString(Id))
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "Id", Id))
                .toOpportunityDto();
    }

    @Override
    public OpportunityDto saveOpportunity(OpportunityPayload payload, String cpf) {
        Opportunity opportunity = payload.toOpportunity();

        opportunity.setPostedBy(userRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cpf", "")));

        opportunity.setNecessarySkills(skillService.convertToSkills(payload.necessarySkills()));

        opportunity.setStatus(OpportunityStatus.valueOf(payload.status()));

        return opportunityRepository.save(opportunity).toOpportunityDto();
    }

    @Override
    public OpportunityDto updateOpportunity(OpportunityPayload payload, String id) {
        Opportunity opportunity = opportunityRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "Id", id));

        opportunity.setTitle(payload.title());
        opportunity.setDescription(payload.description());
        opportunity.setStartDateTime(payload.startDateTime());
        opportunity.setEndDateTime(payload.endDateTime());
        opportunity.setTimeLoad(payload.timeLoad());
        opportunity.setLocal(payload.local());
        opportunity.setValue(payload.value());

        opportunity.setNecessarySkills(skillService.convertToSkills(payload.necessarySkills()));
        
        opportunity.setStatus(OpportunityStatus.valueOf(payload.status()));

        return opportunityRepository.save(opportunity).toOpportunityDto();
    }

    @Override
    public void deactivateOpportunity(String id, String cpf) {
        Opportunity opportunity = opportunityRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "Id", id));

        if (!opportunity.getPostedBy().getCpf().equals(cpf))
            throw new UnauthorizedException("você não pode deletar uma oportunidade que não é sua");

        opportunity.setStatus(OpportunityStatus.CANCELED);

        opportunityRepository.save(opportunity);
    }

    @Override
    public void selectCandidate(String opportunityId, String candidateUsername) {
        Opportunity opportunity = opportunityRepository.findById(UUID.fromString(opportunityId))
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "Id", opportunityId));

        if (opportunity.getStatus() != OpportunityStatus.CREATED)
            throw new IllegalArgumentException("Opportunity is not open, hence you cannot select a candidate");

        opportunity.setSelectedCandidate(userRepository.findByUsername(candidateUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", candidateUsername)));

        opportunity.setStatus(OpportunityStatus.CLOSED);

        opportunityRepository.save(opportunity);
    }


}
