package br.com.ibico.api.repositories;

import br.com.ibico.api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
