package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment,String> {
}
