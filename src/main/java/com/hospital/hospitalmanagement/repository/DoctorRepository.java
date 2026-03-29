package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,String>
{

    Optional<Doctor> findByUser(User user);

    Optional<Doctor> findByUserId(String userId);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findByStatus(Doctor.DoctorStatus status);

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    @Query("SELECT d FROM Doctor d WHERE " +
            "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Doctor> searchDoctors(@Param("query") String query);

    @Query("SELECT DISTINCT d.specialization FROM Doctor d " +
            "WHERE d.status = 'ACTIVE' ORDER BY d.specialization")
    List<String> findAllSpecializations();

}
