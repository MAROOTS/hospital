package com.hospital.hospitalmanagement.dto.response;
import com.hospital.hospitalmanagement.model.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MedicalRecordResponse {
    private String id;

    // Patient info
    private String patientId;
    private String patientName;
    private String patientEmail;

    // Doctor info
    private String doctorId;
    private String doctorName;
    private String doctorSpecialization;

    // Appointment info
    private String appointmentId;

    // Visit details
    private LocalDate visitDate;
    private String chiefComplaint;

    // Vitals
    private Double temperature;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double weight;
    private Double height;
    private Integer oxygenSaturation;
    private String bloodPressure;

    // Clinical details
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String labTests;
    private String labResults;
    private String notes;

    // Follow up
    private LocalDate followUpDate;
    private String status;
    private LocalDateTime createdAt;

    public static MedicalRecordResponse from(MedicalRecord record) {
        String bp = null;
        if (record.getBloodPressureSystolic() != null &&
                record.getBloodPressureDiastolic() != null) {
            bp = record.getBloodPressureSystolic() + "/" +
                    record.getBloodPressureDiastolic() + " mmHg";
        }

        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getUser().getFullName())
                .patientEmail(record.getPatient().getUser().getEmail())
                .doctorId(record.getDoctor().getId())
                .doctorName(record.getDoctor().getUser().getFullName())
                .doctorSpecialization(record.getDoctor().getSpecialization())
                .appointmentId(record.getAppointment() != null ?
                        record.getAppointment().getId() : null)
                .visitDate(record.getVisitDate())
                .chiefComplaint(record.getChiefComplaint())
                .temperature(record.getTemperature())
                .bloodPressureSystolic(record.getBloodPressureSystolic())
                .bloodPressureDiastolic(record.getBloodPressureDiastolic())
                .bloodPressure(bp)
                .heartRate(record.getHeartRate())
                .respiratoryRate(record.getRespiratoryRate())
                .weight(record.getWeight())
                .height(record.getHeight())
                .oxygenSaturation(record.getOxygenSaturation())
                .symptoms(record.getSymptoms())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .prescription(record.getPrescription())
                .labTests(record.getLabTests())
                .labResults(record.getLabResults())
                .notes(record.getNotes())
                .followUpDate(record.getFollowUpDate())
                .status(record.getStatus().name())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
