package com.hospital.hospitalmanagement.dto.response;
import com.hospital.hospitalmanagement.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Builder
@AllArgsConstructor
public class PatientResponse {
    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String address;
    private String city;
    private String country;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String allergies;
    private String chronicConditions;
    private String currentMedications;
    private Double height;
    private Double weight;
    private String insuranceProvider;
    private String insuranceNumber;
    private String profileImage;
    private LocalDateTime createdAt;

    public static PatientResponse from(Patient patient) {
        int age = Period.between(
                patient.getDateOfBirth(), LocalDate.now()).getYears();

        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .fullName(patient.getUser().getFullName())
                .email(patient.getUser().getEmail())
                .phone(patient.getUser().getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .age(age)
                .gender(patient.getGender().name())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .city(patient.getCity())
                .country(patient.getCountry())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .emergencyContactRelation(patient.getEmergencyContactRelation())
                .allergies(patient.getAllergies())
                .chronicConditions(patient.getChronicConditions())
                .currentMedications(patient.getCurrentMedications())
                .height(patient.getHeight())
                .weight(patient.getWeight())
                .insuranceProvider(patient.getInsuranceProvider())
                .insuranceNumber(patient.getInsuranceNumber())
                .profileImage(patient.getUser().getProfileImage())
                .createdAt(patient.getCreatedAt())
                .build();
    }
}
