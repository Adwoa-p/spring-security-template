package spring.security.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.temp.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
