package com.hospital.hospitalmanagement.dto.response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.hospitalmanagement.dto.InvoiceItem;
import com.hospital.hospitalmanagement.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class InvoiceResponse {
    private String id;
    private String invoiceNumber;

    // Patient info
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    // Doctor info
    private String doctorId;
    private String doctorName;
    private String doctorSpecialization;
    // Appointment
    private String appointmentId;
    // Financial details
    private List<InvoiceItem> items;
    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double discount;
    private Double totalAmount;
    // Payment
    private String status;
    private String paymentMethod;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String notes;
    private LocalDateTime createdAt;

    public static InvoiceResponse from(Invoice invoice){
        List<InvoiceItem> items = null;
        try {
            if (invoice.getItems() !=null){
                ObjectMapper mapper = new ObjectMapper();
                items = mapper.readValue(invoice.getItems(),
                        new TypeReference<List<InvoiceItem>>() {
                        }
                );
            }
        }catch (Exception e){
            items = List.of();
        }
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .patientId(invoice.getPatient().getId())
                .patientName(invoice.getPatient().getUser().getFullName())
                .patientEmail(invoice.getPatient().getUser().getEmail())
                .patientPhone(invoice.getPatient().getUser().getPhone())
                .doctorId(invoice.getDoctor().getId())
                .doctorName(invoice.getDoctor().getUser().getFullName())
                .doctorSpecialization(invoice.getDoctor().getSpecialization())
                .appointmentId(invoice.getAppointment() != null ?
                        invoice.getAppointment().getId() : null)
                .items(items)
                .subtotal(invoice.getSubtotal())
                .taxRate(invoice.getTaxRate())
                .taxAmount(invoice.getTaxAmount())
                .discount(invoice.getDiscount())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus().name())
                .paymentMethod(invoice.getPaymentMethod() != null ?
                        invoice.getPaymentMethod().name() : null)
                .dueDate(invoice.getDueDate())
                .paidDate(invoice.getPaidDate())
                .notes(invoice.getNotes())
                .createdAt(invoice.getCreatedAt())
                .build();
    }
    }

