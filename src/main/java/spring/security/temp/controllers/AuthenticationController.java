package spring.security.temp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.temp.models.*;
import spring.security.temp.repository.RefreshTokenRepository;
import spring.security.temp.security.JwtService;
import spring.security.temp.services.AuthenticationService;
import spring.security.temp.services.RefreshTokenService;

import java.util.Map;

// this is where we handle login, signup and getting JWT tokens
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> signup(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.signup(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.signin(authenticationRequest), HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return authenticationService.confirmToken(token);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<GeneralResponse> resendVerification(@RequestBody EmailRequest emailRequest){
        return new ResponseEntity<>(authenticationService.resendVerification(emailRequest), HttpStatus.OK);
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload){
//        String requestToken = payload.get("refreshToken");
//        return refreshTokenRepository.findByToken(requestToken)
//                .map(token -> {
//                    if (refreshTokenService.isTokenExpired(token)) {
//                        refreshTokenRepository.delete(token);
//                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
//                    }
//                    String newJwt = jwtService.generateToken(token.getUser());
//                    return ResponseEntity.ok(Map.of("token", newJwt));
//                })
//                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
//        String requestToken = payload.get("refreshToken");
//
//        if (requestToken == null || requestToken.isBlank()) {
//            return ResponseEntity.badRequest().body("Refresh token is required.");
//        }
//
//        return refreshTokenRepository.findByToken(requestToken)
//                .map(token -> {
//                    refreshTokenRepository.delete(token);
//                    return ResponseEntity.ok("Logged out successfully.");
//                })
//                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
//    }

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        final String authHeader = httpServletRequest.getHeader("Authorization");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<GeneralResponse> refreshToken(@RequestBody PasswordResetRequest passwordResetRequest){
        return new ResponseEntity<>(authenticationService.forgotPassword(passwordResetRequest), HttpStatus.OK);
    }
}
