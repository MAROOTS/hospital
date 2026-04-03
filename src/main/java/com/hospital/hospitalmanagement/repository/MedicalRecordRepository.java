package com.hospital.hospitalmanagement.repository;
import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.model.MedicalRecord;
import com.hospital.hospitalmanagement.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,String>{

    List<MedicalRecord> findByPatientOrderByVisitDateDesc(Patient patient);

    List<MedicalRecord> findByDoctorOrderByVisitDateDesc(Doctor doctor);

    List<MedicalRecord> findByAppointmentId(String appointmentId);

    // Search records by diagnosis or symptoms
    @Query("SELECT m FROM MedicalRecord m WHERE " +
            "m.patient = :patient AND (" +
            "LOWER(m.diagnosis) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.symptoms) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(m.chiefComplaint) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<MedicalRecord> searchByPatient(
            @Param("patient") Patient patient,
            @Param("query") String query);

    // Get records within a date range
    @Query("SELECT m FROM MedicalRecord m WHERE " +
            "m.patient = :patient AND " +
            "m.visitDate BETWEEN :startDate AND :endDate " +
            "ORDER BY m.visitDate DESC")
    List<MedicalRecord> findByPatientAndDateRange(
            @Param("patient") Patient patient,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Count records per patient
    long countByPatient(Patient patient);

}
