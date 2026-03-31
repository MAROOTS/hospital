package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Patient;
import com.hospital.hospitalmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,String> {
    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserId(String userId);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.user.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.user.phone) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Patient> searchPatients(@Param("query") String query);

    @Query("SELECT p FROM Patient p WHERE p.bloodGroup = :bloodGroup")
    List<Patient> findByBloodGroup(@Param("bloodGroup") String bloodGroup);
}
