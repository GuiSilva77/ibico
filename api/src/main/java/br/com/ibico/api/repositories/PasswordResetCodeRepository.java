package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, UUID> {
}
