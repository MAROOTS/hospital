package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.request.CreatePatientRequest;
import com.hospital.hospitalmanagement.dto.request.UpdatePatientRequest;
import com.hospital.hospitalmanagement.dto.response.PatientResponse;
import com.hospital.hospitalmanagement.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient management endpoints")
@SecurityRequirement(name = "Bearer Authentication")

public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Complete patient profile after registration")
    @PostMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> createProfile(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity.ok(
                patientService.createProfile(email, request));
    }

    @Operation(summary = "Get all patients")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @Operation(summary = "Get patient by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @Operation(summary = "Get my patient profile")
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getMyProfile(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(patientService.getMyProfile(email));
    }

    @Operation(summary = "Search patients by name, email or phone")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientResponse>> searchPatients(
            @RequestParam String query) {
        return ResponseEntity.ok(patientService.searchPatients(query));
    }

    @Operation(summary = "Update my patient profile")
    @PutMapping("/my-profile")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> updateMyProfile(
            @AuthenticationPrincipal String email,
            @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(
                patientService.updateProfile(email, request));
    }

    @Operation(summary = "Admin update patient profile")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientResponse> adminUpdateProfile(
            @PathVariable String id,
            @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(
                patientService.adminUpdateProfile(id, request));
    }
}
