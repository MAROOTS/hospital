package com.hospital.hospitalmanagement.dto.request;

import com.hospital.hospitalmanagement.model.Patient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CreatePatientRequest {
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Patient.Gender gender;

    private String bloodGroup;
    private String address;
    private String city;
    private String country;

    // Emergency contact
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;

    // Medical info
    private String allergies;
    private String chronicConditions;
    private String currentMedications;
    private Double height;
    private Double weight;

    // Insurance
    private String insuranceProvider;
    private String insuranceNumber;
}
