package spring.security.temp.services;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.temp.models.*;
import spring.security.temp.models.User;
import spring.security.temp.repository.UserRepository;
import spring.security.temp.security.JwtService;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService service;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository, JwtService service, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Sign Up to use App")
    public RegistrationResponse signup(RegistrationRequest registrationRequest) {
        var user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .enabled(true)
                .build();
        userRepository.save(user);
        return RegistrationResponse.builder()
                .message(String.format("User with name %s %s created successfully", registrationRequest.getFirstName(), registrationRequest.getLastName()))
                .build();
    }

    @Operation(summary = "Sign In to access personal data")
    public AuthenticationResponse signin(AuthenticationRequest authenticationRequest) {
        System.out.println("Starting Authentication");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername()
                        , authenticationRequest.getPassword()
                )
        );
        System.out.println("User Authenticated: " + authenticationRequest.getUsername());

        var user = userRepository.findByEmail(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("User Found: " + authenticationRequest.getUsername());

        var token = service.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .message("User has signed in successfully")
                .build();
    }

}
