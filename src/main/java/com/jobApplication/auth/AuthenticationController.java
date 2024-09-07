package com.jobApplication.auth;

import com.jobApplication.Exception.CheckEmailException;
import com.jobApplication.Exception.EmailAlreadyInUseException;
import com.jobApplication.config.JwtService;
import com.jobApplication.user.User;
import com.jobApplication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public static final String TOKEN_PREFIX ="Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws EmailAlreadyInUseException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)  {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new CheckEmailException("Email is already in use. Please try a different email address.");
            }
            return ResponseEntity.ok(false);
        } catch (CheckEmailException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(true);
        }
    }


    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);

    }


}
