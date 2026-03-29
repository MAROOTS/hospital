package com.hospital.hospitalmanagement.dto.response;

import com.hospital.hospitalmanagement.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class DoctorResponse {
    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private String qualification;
    private String licenseNumber;
    private Integer experienceYears;
    private String bio;
    private Double consultationFee;
    private List<String> availableDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;
    private String status;
    private String profileImage;

    public static DoctorResponse from(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .licenseNumber(doctor.getLicenseNumber())
                .experienceYears(doctor.getExperienceYears())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .availableDays(doctor.getAvailableDays())
                .startTime(doctor.getStartTime())
                .endTime(doctor.getEndTime())
                .slotDuration(doctor.getSlotDuration())
                .status(doctor.getStatus().name())
                .profileImage(doctor.getUser().getProfileImage())
                .build();
    }
}
