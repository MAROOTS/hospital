package com.hospital.hospitalmanagement.dto.request;
import com.hospital.hospitalmanagement.model.Appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class BookAppointmentRequest {
    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType type;

    @NotBlank(message = "Reason for visit is required")
    private String reason;

    private String notes;
}
