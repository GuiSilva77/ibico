package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Candidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, UUID> {
    Page<Candidature> findAllByCandidate_Cpf(String cpf, Pageable pageable);
    Page<Candidature> findAllByOpportunity_Id(UUID opportunityId, Pageable pageable);
}
