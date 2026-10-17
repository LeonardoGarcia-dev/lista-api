
package com.leonardo.listapi.controller;

import com.leonardo.listapi.dto.RegisterRequest;
import com.leonardo.listapi.entity.User;
import com.leonardo.listapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.leonardo.listapi.dto.LoginRequest;
import com.leonardo.listapi.dto.LoginResponse;
import com.leonardo.listapi.service.JwtService;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.register(request);

        Map<String, Object> response = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.authenticate(request);

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(token, "Bearer")
        );
    }
}
