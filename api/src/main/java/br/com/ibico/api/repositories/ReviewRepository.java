package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findAllByOpportunity_Id(UUID id, Pageable pageable);

    Set<Review> findAllByOpportunity_PostedBy_Id(UUID id);
}
