package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Oportunity;
import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Candidature;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.CandidatureDto;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.CandidatureRepository;
import br.com.ibico.api.repositories.OportunityRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.CandidatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CandidatureServiceImpl implements CandidatureService {
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final OportunityRepository oportunityRepository;

    public CandidatureServiceImpl(CandidatureRepository candidatureRepository, UserRepository userRepository, OportunityRepository oportunityRepository) {
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.oportunityRepository = oportunityRepository;
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
    public Response<CandidatureDto> findCandidaturesByOportunityId(String oportunityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Candidature> candidaturePage = candidatureRepository
                .findAllByOportunity_Id(UUID.fromString(oportunityId),
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
    public CandidatureDto createCandidature(String cpf, String oportunityId) {
        User candidate = userRepository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("user", "cpf", ""));
        Oportunity oportunity = oportunityRepository.findById(UUID.fromString(oportunityId)).orElseThrow(() -> new ResourceNotFoundException("oportunity", "id", oportunityId));

        Candidature candidature = new Candidature(candidate, oportunity);

        return candidatureRepository.save(candidature).toCandidatureDto();
    }

    @Override
    public void deleteCandidature(String candidatureId) {
        Candidature candidature = candidatureRepository.findById(UUID.fromString(candidatureId)).orElseThrow(() -> new ResourceNotFoundException("candidature", "id", candidatureId));

        candidatureRepository.delete(candidature);
    }
}
