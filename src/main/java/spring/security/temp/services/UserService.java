package spring.security.temp.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.temp.exceptions.ResourceNotFoundException;
import spring.security.temp.models.*;
import spring.security.temp.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void enableUser(String email){

    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth==null || !auth.isAuthenticated()) {
            throw new RuntimeException("Authentication required");
        }
        String username = auth.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not Found"));

    }

    public ResponseDto<UserResponseDto> getCurrentUser(){
        User user = getAuthenticatedUser();
        UserResponseDto userResponseDto = UserMapper.toDTo(user);


        return ResponseDto.<UserResponseDto>builder()
                .message("Returning Authenticated User")
                .response(userResponseDto)
                .build();

    }

    // update user details - for users
    public ResponseDto<UserResponseDto> updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        userRepository.save(user);
        UserResponseDto userDto = UserMapper.toDTo(user);
        return ResponseDto.<UserResponseDto>builder()
                .message("User deleted successfully")
                .response(userDto)
                .build();
    }

    // delete user - users
    public ResponseDto<String> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsDeleted(true);
        user.setEnabled(false);
        user.setLocked(true);
        userRepository.save(user);
        return ResponseDto.<String>builder()
                .message("User deleted successfully")
                .build();
    }

    public ResponseDto<String> updatePassword(Long id, PasswordResetRequest passwordRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            String password = passwordEncoder.encode(passwordRequest.getNewPassword());
            user.setPassword(password);
        }
        userRepository.save(user);
        return ResponseDto.<String>builder()
                .message("User password updated successfully")
                .build();
    }
}
