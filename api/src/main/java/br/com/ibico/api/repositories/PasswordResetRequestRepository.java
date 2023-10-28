package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, UUID> {
    Optional<PasswordResetRequest> findByAccessToken(String accessToken);
}
