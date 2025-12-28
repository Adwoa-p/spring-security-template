package spring.security.temp.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.temp.models.AuthenticationRequest;
import spring.security.temp.models.AuthenticationResponse;
import spring.security.temp.models.RegistrationRequest;
import spring.security.temp.models.RegistrationResponse;
import spring.security.temp.services.AuthenticationService;

// this is where we handle login, signup and getting JWT tokens
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> signup(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.signup(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.signin(authenticationRequest), HttpStatus.OK);
    }

}
