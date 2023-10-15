package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolesRepsitory extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
