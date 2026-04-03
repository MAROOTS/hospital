package com.hospital.hospitalmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CreateMedicalRecordRequest {
    @NotBlank(message = "Patient ID is required")
    private String patientId;

    private String appointmentId;

    @NotNull(message = "Visit date is required")
    private LocalDate visitDate;

    @NotBlank(message = "Chief complaint is required")
    private String chiefComplaint;

    // Vitals
    private Double temperature;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double weight;
    private Double height;
    private Integer oxygenSaturation;

    // Clinical details
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String treatment;
    private String prescription;
    private String labTests;
    private String labResults;
    private String notes;
    private LocalDate followUpDate;
}
