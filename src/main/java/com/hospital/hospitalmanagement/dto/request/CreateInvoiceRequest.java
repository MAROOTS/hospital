package com.hospital.hospitalmanagement.dto.request;
import com.hospital.hospitalmanagement.dto.InvoiceItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateInvoiceRequest {
    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    private String appointmentId;

    @NotEmpty(message = "At least one invoice item is required")
    private List<InvoiceItem> items;

    private Double discount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private String notes;
}
