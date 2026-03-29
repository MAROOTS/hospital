package com.hospital.hospitalmanagement.dto.response;

import com.hospital.hospitalmanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private boolean active;

    public static AuthResponse from(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }
}
