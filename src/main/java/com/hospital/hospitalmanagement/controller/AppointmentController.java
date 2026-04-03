package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.request.BookAppointmentRequest;
import com.hospital.hospitalmanagement.dto.request.UpdateAppointmentRequest;
import com.hospital.hospitalmanagement.dto.response.AppointmentResponse;
import com.hospital.hospitalmanagement.service.AppointmentService;
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
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments",
        description = "Appointment booking and management")
@SecurityRequirement(name = "Bearer Authentication")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Book a new appointment")
    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody BookAppointmentRequest request) {
        return ResponseEntity.ok(
                appointmentService.bookAppointment(email, request));
    }

    @Operation(summary = "Get my appointments (Patient)")
    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                appointmentService.getMyAppointments(email));
    }

    @Operation(summary = "Get my appointments (Doctor)")
    @GetMapping("/doctor-appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments(email));
    }

    @Operation(summary = "Get today's appointments (Doctor)")
    @GetMapping("/today")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getTodaysAppointments(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                appointmentService.getTodaysAppointments(email));
    }

    @Operation(summary = "Get all appointments (Admin/Receptionist)")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @Operation(summary = "Get appointment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id));
    }

    @Operation(summary = "Get available slots for a doctor on a date")
    @GetMapping("/available-slots")
    public ResponseEntity<List<String>> getAvailableSlots(
            @RequestParam String doctorId,
            @RequestParam @DateTimeFormat(iso =
                    DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                appointmentService.getAvailableSlots(doctorId, date));
    }

    @Operation(summary = "Update appointment")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable String id,
            @RequestBody UpdateAppointmentRequest request) {
        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, request));
    }

    @Operation(summary = "Cancel an appointment")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id, reason));
    }
}
