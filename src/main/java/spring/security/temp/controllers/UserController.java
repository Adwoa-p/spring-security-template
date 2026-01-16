package spring.security.temp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.temp.models.PasswordResetRequest;
import spring.security.temp.models.ResponseDto;
import spring.security.temp.models.UserRequestDto;
import spring.security.temp.models.UserResponseDto;
import spring.security.temp.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserResponseDto>> getCurrentUser() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update current user details")
    public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.updateUser(id, userRequestDto), HttpStatus.OK);
    }

    @PatchMapping("{id}/reset")
    @Operation(summary = "Update User's password")
    public ResponseEntity<ResponseDto<String>> updateUserPassword(@PathVariable Long id, @RequestBody PasswordResetRequest passwordRequest) {
        return new ResponseEntity<>(userService.updatePassword(id, passwordRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
