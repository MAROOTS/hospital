package com.hospital.hospitalmanagement.service;
import com.hospital.hospitalmanagement.dto.request.CreatePatientRequest;
import com.hospital.hospitalmanagement.dto.request.UpdatePatientRequest;
import com.hospital.hospitalmanagement.dto.response.PatientResponse;
import com.hospital.hospitalmanagement.model.Patient;
import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.PatientRepository;
import com.hospital.hospitalmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    // Called after registration to complete profile
    @Transactional
    public PatientResponse createProfile(String email,
                                         CreatePatientRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (patientRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Patient profile already exists");
        }

        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .emergencyContactRelation(request.getEmergencyContactRelation())
                .allergies(request.getAllergies())
                .chronicConditions(request.getChronicConditions())
                .currentMedications(request.getCurrentMedications())
                .height(request.getHeight())
                .weight(request.getWeight())
                .insuranceProvider(request.getInsuranceProvider())
                .insuranceNumber(request.getInsuranceNumber())
                .build();

        patientRepository.save(patient);
        return PatientResponse.from(patient);
    }

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(PatientResponse::from)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return PatientResponse.from(patient);
    }

    public PatientResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found." +
                                " Please complete your profile."));
        return PatientResponse.from(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query)
                .stream()
                .map(PatientResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updateProfile(String email,
                                         UpdatePatientRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException(
                        "Patient profile not found"));

        if (request.getDateOfBirth() != null)
            patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null)
            patient.setGender(request.getGender());
        if (request.getBloodGroup() != null)
            patient.setBloodGroup(request.getBloodGroup());
        if (request.getAddress() != null)
            patient.setAddress(request.getAddress());
        if (request.getCity() != null)
            patient.setCity(request.getCity());
        if (request.getCountry() != null)
            patient.setCountry(request.getCountry());
        if (request.getEmergencyContactName() != null)
            patient.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null)
            patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getEmergencyContactRelation() != null)
            patient.setEmergencyContactRelation(
                    request.getEmergencyContactRelation());
        if (request.getAllergies() != null)
            patient.setAllergies(request.getAllergies());
        if (request.getChronicConditions() != null)
            patient.setChronicConditions(request.getChronicConditions());
        if (request.getCurrentMedications() != null)
            patient.setCurrentMedications(request.getCurrentMedications());
        if (request.getHeight() != null)
            patient.setHeight(request.getHeight());
        if (request.getWeight() != null)
            patient.setWeight(request.getWeight());
        if (request.getInsuranceProvider() != null)
            patient.setInsuranceProvider(request.getInsuranceProvider());
        if (request.getInsuranceNumber() != null)
            patient.setInsuranceNumber(request.getInsuranceNumber());

        patientRepository.save(patient);
        return PatientResponse.from(patient);
    }

    @Transactional
    public PatientResponse adminUpdateProfile(String patientId,
                                              UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (request.getDateOfBirth() != null)
            patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null)
            patient.setGender(request.getGender());
        if (request.getBloodGroup() != null)
            patient.setBloodGroup(request.getBloodGroup());
        if (request.getAddress() != null)
            patient.setAddress(request.getAddress());
        if (request.getCity() != null)
            patient.setCity(request.getCity());
        if (request.getCountry() != null)
            patient.setCountry(request.getCountry());
        if (request.getEmergencyContactName() != null)
            patient.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null)
            patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getEmergencyContactRelation() != null)
            patient.setEmergencyContactRelation(
                    request.getEmergencyContactRelation());
        if (request.getAllergies() != null)
            patient.setAllergies(request.getAllergies());
        if (request.getChronicConditions() != null)
            patient.setChronicConditions(request.getChronicConditions());
        if (request.getCurrentMedications() != null)
            patient.setCurrentMedications(request.getCurrentMedications());
        if (request.getHeight() != null)
            patient.setHeight(request.getHeight());
        if (request.getWeight() != null)
            patient.setWeight(request.getWeight());
        if (request.getInsuranceProvider() != null)
            patient.setInsuranceProvider(request.getInsuranceProvider());
        if (request.getInsuranceNumber() != null)
            patient.setInsuranceNumber(request.getInsuranceNumber());

        patientRepository.save(patient);
        return PatientResponse.from(patient);
    }
}
