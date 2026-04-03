package com.hospital.hospitalmanagement.dto.response;

import com.hospital.hospitalmanagement.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@Builder
@AllArgsConstructor
public class AppointmentResponse {
    private String id;

    // Doctor info
    private String doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private Double consultationFee;

    // Patient info
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;

    // Appointment details
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String type;
    private String reason;
    private String notes;
    private String cancellationReason;
    private LocalDateTime createdAt;

    public static AppointmentResponse from(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getUser().getFullName())
                .doctorSpecialization(a.getDoctor().getSpecialization())
                .consultationFee(a.getDoctor().getConsultationFee())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getUser().getFullName())
                .patientEmail(a.getPatient().getUser().getEmail())
                .patientPhone(a.getPatient().getUser().getPhone())
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .status(a.getStatus().name())
                .type(a.getType().name())
                .reason(a.getReason())
                .notes(a.getNotes())
                .cancellationReason(a.getCancellationReason())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
