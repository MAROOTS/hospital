package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.request.CreateMedicalRecordRequest;
import com.hospital.hospitalmanagement.dto.request.UpdateMedicalRecordRequest;
import com.hospital.hospitalmanagement.dto.response.MedicalRecordResponse;
import com.hospital.hospitalmanagement.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Medical Records",
        description = "Medical record management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @Operation(summary = "Create a medical record (Doctor only)")
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> createRecord(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody CreateMedicalRecordRequest request) {
        return ResponseEntity.ok(
                medicalRecordService.createRecord(email, request));
    }

    @Operation(summary = "Get my medical records (Patient)")
    @GetMapping("/my-records")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalRecordResponse>> getMyRecords(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                medicalRecordService.getMyRecords(email));
    }

    @Operation(summary = "Get my records as doctor")
    @GetMapping("/doctor-records")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalRecordResponse>> getDoctorRecords(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                medicalRecordService.getDoctorRecords(email));
    }

    @Operation(summary = "Get all records for a patient")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<MedicalRecordResponse>> getPatientRecords(
            @PathVariable String patientId) {
        return ResponseEntity.ok(
                medicalRecordService.getPatientRecords(patientId));
    }

    @Operation(summary = "Get record by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponse> getRecordById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                medicalRecordService.getRecordById(id));
    }

    @Operation(summary = "Search my records by diagnosis or symptoms")
    @GetMapping("/search")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalRecordResponse>> searchMyRecords(
            @AuthenticationPrincipal String email,
            @RequestParam String query) {
        return ResponseEntity.ok(
                medicalRecordService.searchMyRecords(email, query));
    }

    @Operation(summary = "Get records within a date range")
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalRecordResponse>> getRecordsByDateRange(
            @AuthenticationPrincipal String email,
            @RequestParam @DateTimeFormat(iso =
                    DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso =
                    DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(medicalRecordService
                .getRecordsByDateRange(email, startDate, endDate));
    }

    @Operation(summary = "Update a medical record (Doctor only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponse> updateRecord(
            @PathVariable String id,
            @RequestBody UpdateMedicalRecordRequest request) {
        return ResponseEntity.ok(
                medicalRecordService.updateRecord(id, request));
    }

}
