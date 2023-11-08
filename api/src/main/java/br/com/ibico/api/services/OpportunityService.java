package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OpportunityDto;
import br.com.ibico.api.entities.payload.OpportunityPayload;

public interface OpportunityService {

    Response<OpportunityDto> findOpportunities(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    OpportunityDto findOpportunityById(String Id);
    OpportunityDto saveOpportunity(OpportunityPayload payload, String cpf);
    OpportunityDto updateOpportunity(OpportunityPayload opportunityDto, String id);
    void deactivateOpportunity(String id);
    void selectCandidate(String opportunityId, String candidateUsername);
}
