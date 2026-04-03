package com.hospital.hospitalmanagement.dto.request;

import com.hospital.hospitalmanagement.model.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class UpdateAppointmentRequest {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Appointment.AppointmentStatus status;
    private Appointment.AppointmentType type;
    private String reason;
    private String notes;
    private String cancellationReason;
}
