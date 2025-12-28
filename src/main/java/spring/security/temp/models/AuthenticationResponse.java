package spring.security.temp.models;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String message, String token) {
}
