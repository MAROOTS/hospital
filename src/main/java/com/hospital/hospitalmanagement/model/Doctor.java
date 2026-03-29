package com.hospital.hospitalmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Builder
@Entity
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String licenseNumber;

    private Integer experienceYears;

    private String bio;

    // Consultation fee
    private Double consultationFee;

    // Available days e.g. ["MONDAY","WEDNESDAY","FRIDAY"]
    @ElementCollection
    @CollectionTable(name = "doctor_available_days",
            joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "day")
    private List<String> availableDays;

    // Working hours
    private LocalTime startTime;
    private LocalTime endTime;

    // Appointment slot duration in minutes
    @Builder.Default
    private Integer slotDuration = 30;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DoctorStatus status = DoctorStatus.ACTIVE;

    public enum DoctorStatus {
        ACTIVE, ON_LEAVE, INACTIVE
    }
}
