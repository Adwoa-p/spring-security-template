package spring.security.temp.models;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(String message, RefreshToken token) {
}
