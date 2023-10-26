package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.CandidatureDto;

public interface CandidatureService {
    Response<CandidatureDto> findCandidatures(String cpf, int pageNo, int pageSize, String sortBy, String sortDir);

    Response<CandidatureDto> findCandidaturesByOportunityId(String oportunityId, int pageNo, int pageSize, String sortBy, String sortDir);

    CandidatureDto findCandidatureById(String candidatureId);

    CandidatureDto createCandidature(String cpf, String oportunityId);

    void deleteCandidature(String candidatureId);
}
