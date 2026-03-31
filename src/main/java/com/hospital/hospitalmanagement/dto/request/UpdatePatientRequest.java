package com.hospital.hospitalmanagement.dto.request;

import com.hospital.hospitalmanagement.model.Patient;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdatePatientRequest {
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
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
}
