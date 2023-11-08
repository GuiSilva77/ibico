package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Opportunity;
import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Candidature;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.CandidatureDto;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.exceptions.UnauthorizedException;
import br.com.ibico.api.repositories.CandidatureRepository;
import br.com.ibico.api.repositories.OpportunityRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.CandidatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CandidatureServiceImpl implements CandidatureService {
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final OpportunityRepository opportunityRepository;

    public CandidatureServiceImpl(CandidatureRepository candidatureRepository, UserRepository userRepository, OpportunityRepository opportunityRepository) {
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public Response<CandidatureDto> findCandidatures(String cpf, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Candidature> candidaturePage = candidatureRepository
                .findAllByCandidate_Cpf(cpf,
                        PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortDir), sortBy)));

        List<CandidatureDto> candidatures = candidaturePage.get()
                                                .map(Candidature::toCandidatureDto)
                                                .toList();

        return new Response<>(candidatures,
                candidaturePage.getNumber(),
                candidaturePage.getSize(),
                candidaturePage.getNumberOfElements(),
                candidaturePage.getTotalPages(),
                candidaturePage.isLast(),
                false);
    }

    @Override
    public Response<CandidatureDto> findCandidaturesByOpportunityId(String opportunityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String) authentication.getPrincipal();

        Opportunity opportunity = opportunityRepository.findById(UUID.fromString(opportunityId)).orElseThrow(() -> new ResourceNotFoundException("opportunity", "id", opportunityId));

        // if cpf is different from opportunity postedBy cpf, throw unauthorized exception
        if (opportunity.getPostedBy().getCpf().equals(cpf))
            throw new UnauthorizedException("Você não tem permissão para acessar essa oportunidade");

        Page<Candidature> candidaturePage = candidatureRepository
                .findAllByOpportunity_Id(UUID.fromString(opportunityId),
                        PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortDir), sortBy)));

        List<CandidatureDto> candidatures = candidaturePage.get()
                .map(Candidature::toCandidatureDto)
                .toList();

        return new Response<>(candidatures,
                candidaturePage.getNumber(),
                candidaturePage.getSize(),
                candidaturePage.getNumberOfElements(),
                candidaturePage.getTotalPages(),
                candidaturePage.isLast(),
                false);
    }

    @Override
    public CandidatureDto findCandidatureById(String candidatureId) {
        return candidatureRepository.findById(UUID.fromString(candidatureId)).orElseThrow(() -> new ResourceNotFoundException("candidature", "id", candidatureId)).toCandidatureDto();
    }

    @Override
    public CandidatureDto createCandidature(String cpf, String opportunityId) {
        User candidate = userRepository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("user", "cpf", ""));
        Opportunity opportunity = opportunityRepository.findById(UUID.fromString(opportunityId)).orElseThrow(() -> new ResourceNotFoundException("opportunity", "id", opportunityId));

        Candidature candidature = new Candidature(candidate, opportunity);

        return candidatureRepository.save(candidature).toCandidatureDto();
    }

    @Override
    public void deleteCandidature(String candidatureId) {
        Candidature candidature = candidatureRepository.findById(UUID.fromString(candidatureId)).orElseThrow(() -> new ResourceNotFoundException("candidature", "id", candidatureId));

        candidatureRepository.delete(candidature);
    }
}
