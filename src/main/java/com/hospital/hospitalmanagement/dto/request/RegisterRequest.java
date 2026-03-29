package com.hospital.hospitalmanagement.dto.request;

import com.hospital.hospitalmanagement.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Invalid phone number")
    private String phone;

    // Only PATIENT self-registration allowed
    // DOCTOR, ADMIN, RECEPTIONIST created by ADMIN
    private User.Role role = User.Role.PATIENT;
}
