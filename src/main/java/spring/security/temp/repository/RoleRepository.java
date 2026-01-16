package spring.security.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.temp.models.Role;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String admin);
}
