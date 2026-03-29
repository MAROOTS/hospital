package com.hospital.hospitalmanagement.service;

import com.hospital.hospitalmanagement.dto.request.CreateDoctorRequest;
import com.hospital.hospitalmanagement.dto.request.UpdateDoctorRequest;
import com.hospital.hospitalmanagement.dto.response.DoctorResponse;
import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.DoctorRepository;
import com.hospital.hospitalmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorResponse createDoctor(CreateDoctorRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already in use");
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.DOCTOR)
                .build();

        userRepository.save(user);

        // Create doctor profile
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .licenseNumber(request.getLicenseNumber())
                .experienceYears(request.getExperienceYears())
                .bio(request.getBio())
                .consultationFee(request.getConsultationFee())
                .availableDays(request.getAvailableDays())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDuration(request.getSlotDuration() != null ?
                        request.getSlotDuration() : 30)
                .build();

        doctorRepository.save(doctor);
        return DoctorResponse.from(doctor);
    }

    public List<DoctorResponse> getAllDoctors(){
        return doctorRepository.findAll()
                .stream()
                .map(DoctorResponse::from)
                .collect(Collectors.toList());
    }
    public List<DoctorResponse> getActiveDoctors() {
        return doctorRepository.findByStatus(Doctor.DoctorStatus.ACTIVE)
                .stream()
                .map(DoctorResponse::from)
                .collect(Collectors.toList());
    }

    public DoctorResponse getDoctorById(String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return DoctorResponse.from(doctor);
    }

    public DoctorResponse getDoctorByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return DoctorResponse.from(doctor);
    }

    public List<DoctorResponse> searchDoctors(String query) {
        return doctorRepository.searchDoctors(query)
                .stream()
                .map(DoctorResponse::from)
                .collect(Collectors.toList());
    }

    public List<String> getAllSpecializations() {
        return doctorRepository.findAllSpecializations();
    }


    @Transactional
    public DoctorResponse updateDoctor(String id, UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (request.getSpecialization() != null)
            doctor.setSpecialization(request.getSpecialization());
        if (request.getQualification() != null)
            doctor.setQualification(request.getQualification());
        if (request.getBio() != null)
            doctor.setBio(request.getBio());
        if (request.getConsultationFee() != null)
            doctor.setConsultationFee(request.getConsultationFee());
        if (request.getAvailableDays() != null)
            doctor.setAvailableDays(request.getAvailableDays());
        if (request.getStartTime() != null)
            doctor.setStartTime(request.getStartTime());
        if (request.getEndTime() != null)
            doctor.setEndTime(request.getEndTime());
        if (request.getSlotDuration() != null)
            doctor.setSlotDuration(request.getSlotDuration());
        if (request.getStatus() != null)
            doctor.setStatus(Doctor.DoctorStatus.valueOf(request.getStatus()));

        doctorRepository.save(doctor);
        return DoctorResponse.from(doctor);
    }
    @Transactional
    public void deleteDoctor(String id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(Doctor.DoctorStatus.INACTIVE);
        doctor.getUser().setActive(false);
        doctorRepository.save(doctor);
    }
}
