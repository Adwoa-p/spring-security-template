package spring.security.temp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequest {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
