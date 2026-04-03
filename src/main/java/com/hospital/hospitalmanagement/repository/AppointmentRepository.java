package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Appointment;
import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,String> {
    List<Appointment> findByPatientOrderByAppointmentDateDesc(Patient patient);

    List<Appointment> findByDoctorOrderByAppointmentDateDesc(Doctor doctor);

    List<Appointment> findByAppointmentDateAndDoctor(
            LocalDate date, Doctor doctor);

    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    List<Appointment> findByDoctorAndStatus(
            Doctor doctor, Appointment.AppointmentStatus status);

    List<Appointment> findByPatientAndStatus(
            Patient patient, Appointment.AppointmentStatus status);

    // Check if slot is already taken
    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Doctor doctor,
            LocalDate date,
            LocalTime time,
            Appointment.AppointmentStatus status);

    // Get today's appointments for a doctor
    @Query("SELECT a FROM Appointment a WHERE " +
            "a.doctor = :doctor AND " +
            "a.appointmentDate = :date AND " +
            "a.status IN ('PENDING', 'CONFIRMED') " +
            "ORDER BY a.appointmentTime ASC")
    List<Appointment> findTodaysAppointments(
            @Param("doctor") Doctor doctor,
            @Param("date") LocalDate date);

    // Dashboard stats
    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "a.appointmentDate = :date")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "a.status = :status")
    long countByStatus(
            @Param("status") Appointment.AppointmentStatus status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "a.doctor.id = :doctorId AND " +
            "a.appointmentDate = :date")
    long countByDoctorAndDate(
            @Param("doctorId") String doctorId,
            @Param("date") LocalDate date);
}
