package com.hospital.hospitalmanagement.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // Visit details
    @Column(nullable = false)
    private LocalDate visitDate;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    // Vitals
    private Double temperature;       // in Celsius
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;        // beats per minute
    private Integer respiratoryRate;  // breaths per minute
    private Double weight;            // in kg
    private Double height;            // in cm
    private Integer oxygenSaturation; // percentage

    // Clinical details
    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Column(columnDefinition = "TEXT")
    private String labTests;

    @Column(columnDefinition = "TEXT")
    private String labResults;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Follow up
    private LocalDate followUpDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RecordStatus status = RecordStatus.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RecordStatus {
        ACTIVE, ARCHIVED
    }
}
