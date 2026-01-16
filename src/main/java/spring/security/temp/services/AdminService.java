package spring.security.temp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.security.temp.exceptions.ResourceNotFoundException;
import spring.security.temp.models.ResponseDto;
import spring.security.temp.models.User;
import spring.security.temp.models.UserMapper;
import spring.security.temp.models.UserResponseDto;
import spring.security.temp.repository.UserRepository;

import java.util.Set;

@Service
public class AdminService{
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getAllUsers(int pageNo, int pageSize, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    public ResponseDto<UserResponseDto> getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserResponseDto userResponseDto = UserMapper.toDTo(user);
        return ResponseDto.<UserResponseDto>builder()
                .message(String.format("Found user with id %d", id))
                .response(userResponseDto)
                .build();
    }


    //    for admins
    public ResponseDto<String> partialUpdateUser(Long id, Boolean locked, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setLocked(locked);
        user.setEnabled(enabled);
        userRepository.save(user);
        return ResponseDto.<String>builder()
                .message("User updated successfully")
                .build();
    }

}

