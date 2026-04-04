package com.hospital.hospitalmanagement.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Invoice items stored as JSON string
    @Column(columnDefinition = "TEXT")
    private String items;

    @Column(nullable = false)
    private Double subtotal;

    @Builder.Default
    private Double taxRate = 16.0;    // 16% VAT

    private Double taxAmount;

    @Builder.Default
    private Double discount = 0.0;

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate dueDate;
    private LocalDate paidDate;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Calculate tax and total
        if (subtotal != null) {
            taxAmount = subtotal * (taxRate / 100);
            totalAmount = subtotal + taxAmount - (discount != null ? discount : 0);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        UNPAID, PARTIAL, PAID, OVERDUE, CANCELLED
    }

    public enum PaymentMethod {
        CASH, MPESA, CREDIT_CARD, INSURANCE, BANK_TRANSFER
    }
}
