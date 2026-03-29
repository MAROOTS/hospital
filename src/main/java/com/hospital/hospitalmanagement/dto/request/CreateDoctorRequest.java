package com.hospital.hospitalmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
@Data
public class CreateDoctorRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    private Integer experienceYears;

    private String bio;

    @NotNull(message = "Consultation fee is required")
    @Min(value = 0, message = "Fee cannot be negative")
    private Double consultationFee;

    private List<String> availableDays;

    private LocalTime startTime;

    private LocalTime endTime;


    private Integer slotDuration = 30;
}
