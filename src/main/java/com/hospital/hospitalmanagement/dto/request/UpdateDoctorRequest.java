package com.hospital.hospitalmanagement.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
@Data
public class UpdateDoctorRequest {
    private String specialization;
    private String qualification;
    private String bio;
    private Double consultationFee;
    private List<String> availableDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;
    private String status;
}
