package spring.security.temp.services;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.temp.email.EmailSender;
import spring.security.temp.exceptions.ResourceAlreadyExistsException;
import spring.security.temp.exceptions.ResourceNotFoundException;
import spring.security.temp.models.*;
import spring.security.temp.models.User;
import spring.security.temp.repository.UserRepository;
import spring.security.temp.security.JwtService;
import spring.security.temp.token.ConfirmationToken;
import spring.security.temp.token.ConfirmationTokenRepository;
import spring.security.temp.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static spring.security.temp.email.EmailBuilder.buildVerificationEmail;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService service;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public AuthenticationService(UserRepository userRepository, EmailSender emailSender, JwtService service, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ConfirmationTokenService confirmationTokenService, UserService userService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
        this.emailSender = emailSender;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Operation(summary = "Sign Up to use App")
    public RegistrationResponse signup(RegistrationRequest registrationRequest) {
        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with email already present");
        }
        var user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .enabled(true)
                .build();
        userRepository.save(user);

        // create a token and save it using method written
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(registrationRequest.getEmail(), buildVerificationEmail(registrationRequest.getFirstName(), link));

        //send email
        return RegistrationResponse.builder()
                .message(String.format("User with name %s %s created successfully", registrationRequest.getFirstName(), registrationRequest.getLastName()))
                .token(token)
                .build();
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null){
            throw new ResourceAlreadyExistsException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());

        return "Email Confirmed";
    }


    public GeneralResponse forgotPassword(PasswordResetRequest passwordResetRequest){

        if(!Objects.equals(passwordResetRequest.getNewPassword(), passwordResetRequest.getConfirmPassword())) {
            throw new IllegalStateException("Incorrect Password");
        }
        ConfirmationToken token =  confirmationTokenRepository.findByToken(passwordResetRequest.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        if (token.getConfirmedAt() != null || token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResourceAlreadyExistsException("Password Reset Token is expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(user);

        return GeneralResponse.builder()
                .message("Password Reset Successfully")
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

    @Transactional
    public GeneralResponse resendVerification( EmailRequest emailRequest){
        User user = userRepository.findByEmail(emailRequest.email()).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        // create a token and save it using method written

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        System.out.println(token);

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        System.out.println("token is saved");

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(emailRequest.email(), buildVerificationEmail(user.getFirstName(), link));
        System.out.println("email has been sent");

        //send email
        return GeneralResponse.builder()
                .message("Verification token resent")
                .build();
    }



}
