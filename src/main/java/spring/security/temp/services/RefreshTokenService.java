package spring.security.temp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.security.temp.models.RefreshToken;
import spring.security.temp.models.RefreshTokenResponse;
import spring.security.temp.repository.RefreshTokenRepository;
import spring.security.temp.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshTokenResponse refreshToken(Long userId) {
        var token = new RefreshToken();
        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(token);

        return RefreshTokenResponse.builder()
                .message("Token refreshed successfully")
                .token(token)
                .build();
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
