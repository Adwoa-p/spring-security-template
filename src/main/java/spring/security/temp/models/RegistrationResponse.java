package spring.security.temp.models;

import lombok.Builder;

@Builder
public record RegistrationResponse(String message, String status) {
}
