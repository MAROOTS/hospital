package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.request.LoginRequest;
import com.hospital.hospitalmanagement.dto.request.RegisterRequest;
import com.hospital.hospitalmanagement.dto.response.AuthResponse;
import com.hospital.hospitalmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Register, login and get current user")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register a new patient account")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Get currently logged in user")
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getMe(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(authService.getMe(email));
    }
}
