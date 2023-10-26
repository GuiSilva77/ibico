package br.com.ibico.api.entities.payload;

import java.util.UUID;

public record CandidaturePayload(String username, UUID oportunityId) {
}
