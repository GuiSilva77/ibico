package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OportunityDto;
import br.com.ibico.api.entities.payload.OportunityPayload;

public interface OportunityService {

    Response<OportunityDto> findOportunities(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    OportunityDto findOportunityById(String Id);
    OportunityDto saveOportunity(OportunityPayload payload, String cpf);
    OportunityDto updateOportunity(OportunityPayload oportunityDto, String id, String cpf);
    void deactivateOportunity(String id, String cpf);
}
