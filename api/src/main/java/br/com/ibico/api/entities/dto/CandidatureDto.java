package br.com.ibico.api.entities.dto;

import br.com.ibico.api.entities.Candidature;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link Candidature}
 */
public record CandidatureDto(UUID id, LocalDateTime candidatureDate, String candidateName, String candidateUsername,
                             String candidateImgURL, UUID oportunityId) implements Serializable {
}