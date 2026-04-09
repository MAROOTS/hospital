package com.hospital.hospitalmanagement.service;

import com.hospital.hospitalmanagement.dto.request.LoginRequest;
import com.hospital.hospitalmanagement.dto.request.RegisterRequest;
import com.hospital.hospitalmanagement.dto.response.AuthResponse;
import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.UserRepository;
import com.hospital.hospitalmanagement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final NotificationService notificationService;
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email in use");
        }
        if (userRepository.existsByPhone(request.getPhone())){
            throw new RuntimeException("phone number in use");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.PATIENT)
                .build();
        userRepository.save(user);
        userRepository.findByRole(User.Role.ADMIN).forEach(admin ->
                notificationService.notifyNewPatientRegistered(
                        admin.getEmail(),
                        user.getFullName()
                )
        );
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name()
        );
        return AuthResponse.from(user,token);
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid email or password"));
        if (!user.isActive()){
            throw new RuntimeException("Account is deactivated. " +
                    "Please contact admin.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name()
        );
        return AuthResponse.from(user,token);
    }
    public AuthResponse getMe(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name()
        );
        return AuthResponse.from(user,token);
    }
}
