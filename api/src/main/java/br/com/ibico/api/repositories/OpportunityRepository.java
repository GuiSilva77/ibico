package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, UUID> {
    Page<Opportunity> findByTitle(String title, Pageable pageable);
}
