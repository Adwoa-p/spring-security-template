package spring.security.temp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String fullName;
    private Boolean locked;
    private Boolean enabled;
}
