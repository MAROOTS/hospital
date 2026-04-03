package com.hospital.hospitalmanagement.dto.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdateMedicalRecordRequest {
    private LocalDate visitDate;
    private String chiefComplaint;
    private Double temperature;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double weight;
    private Double height;
    private Integer oxygenSaturation;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String labTests;
    private String labResults;
    private String notes;
    private LocalDate followUpDate;
    private String status;
}
