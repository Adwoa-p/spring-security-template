package spring.security.temp.models;

import spring.security.temp.models.UserResponseDto;
import spring.security.temp.models.User;

public class UserMapper {
    public static UserResponseDto toDTo(User user) {
        return new UserResponseDto(
                user.getEmail(),
                user.getFirstName() + "" + user.getLastName(),
                user.getLocked(),
                user.getEnabled()
        );
    }
}
