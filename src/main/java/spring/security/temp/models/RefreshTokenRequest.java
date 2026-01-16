package spring.security.temp.models;

import lombok.Builder;
import spring.security.temp.token.ConfirmationToken;

@Builder
public record RefreshTokenRequest (String token) {
}
