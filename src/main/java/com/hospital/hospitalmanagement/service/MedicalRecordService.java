package com.hospital.hospitalmanagement.service;

import com.hospital.hospitalmanagement.dto.request.CreateMedicalRecordRequest;
import com.hospital.hospitalmanagement.dto.request.UpdateMedicalRecordRequest;
import com.hospital.hospitalmanagement.dto.response.MedicalRecordResponse;
import com.hospital.hospitalmanagement.model.*;
import com.hospital.hospitalmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public MedicalRecordResponse createRecord(String doctorEmail,
                                              CreateMedicalRecordRequest request) {

        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepository.findByUser(doctorUser)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository
                    .findById(request.getAppointmentId())
                    .orElse(null);

            // Mark appointment as completed
            if (appointment != null) {
                appointment.setStatus(
                        Appointment.AppointmentStatus.COMPLETED);
                appointmentRepository.save(appointment);
            }
        }

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .visitDate(request.getVisitDate())
                .chiefComplaint(request.getChiefComplaint())
                .temperature(request.getTemperature())
                .bloodPressureSystolic(request.getBloodPressureSystolic())
                .bloodPressureDiastolic(request.getBloodPressureDiastolic())
                .heartRate(request.getHeartRate())
                .respiratoryRate(request.getRespiratoryRate())
                .weight(request.getWeight())
                .height(request.getHeight())
                .oxygenSaturation(request.getOxygenSaturation())
                .symptoms(request.getSymptoms())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .prescription(request.getPrescription())
                .labTests(request.getLabTests())
                .labResults(request.getLabResults())
                .notes(request.getNotes())
                .followUpDate(request.getFollowUpDate())
                .build();

        medicalRecordRepository.save(record);
        return MedicalRecordResponse.from(record);
    }

    public List<MedicalRecordResponse> getPatientRecords(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalRecordRepository
                .findByPatientOrderByVisitDateDesc(patient)
                .stream()
                .map(MedicalRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getMyRecords(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));

        return medicalRecordRepository
                .findByPatientOrderByVisitDateDesc(patient)
                .stream()
                .map(MedicalRecordResponse::from)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponse getRecordById(String id) {
        return MedicalRecordResponse.from(
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medical record not found")));
    }

    public List<MedicalRecordResponse> getDoctorRecords(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found"));

        return medicalRecordRepository
                .findByDoctorOrderByVisitDateDesc(doctor)
                .stream()
                .map(MedicalRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> searchMyRecords(
            String email, String query) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));

        return medicalRecordRepository.searchByPatient(patient, query)
                .stream()
                .map(MedicalRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getRecordsByDateRange(
            String email, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));

        return medicalRecordRepository
                .findByPatientAndDateRange(patient, startDate, endDate)
                .stream()
                .map(MedicalRecordResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordResponse updateRecord(String id,
                                              UpdateMedicalRecordRequest request) {

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Medical record not found"));

        if (request.getVisitDate() != null)
            record.setVisitDate(request.getVisitDate());
        if (request.getChiefComplaint() != null)
            record.setChiefComplaint(request.getChiefComplaint());
        if (request.getTemperature() != null)
            record.setTemperature(request.getTemperature());
        if (request.getBloodPressureSystolic() != null)
            record.setBloodPressureSystolic(request.getBloodPressureSystolic());
        if (request.getBloodPressureDiastolic() != null)
            record.setBloodPressureDiastolic(
                    request.getBloodPressureDiastolic());
        if (request.getHeartRate() != null)
            record.setHeartRate(request.getHeartRate());
        if (request.getRespiratoryRate() != null)
            record.setRespiratoryRate(request.getRespiratoryRate());
        if (request.getWeight() != null)
            record.setWeight(request.getWeight());
        if (request.getHeight() != null)
            record.setHeight(request.getHeight());
        if (request.getOxygenSaturation() != null)
            record.setOxygenSaturation(request.getOxygenSaturation());
        if (request.getSymptoms() != null)
            record.setSymptoms(request.getSymptoms());
        if (request.getDiagnosis() != null)
            record.setDiagnosis(request.getDiagnosis());
        if (request.getTreatment() != null)
            record.setTreatment(request.getTreatment());
        if (request.getPrescription() != null)
            record.setPrescription(request.getPrescription());
        if (request.getLabTests() != null)
            record.setLabTests(request.getLabTests());
        if (request.getLabResults() != null)
            record.setLabResults(request.getLabResults());
        if (request.getNotes() != null)
            record.setNotes(request.getNotes());
        if (request.getFollowUpDate() != null)
            record.setFollowUpDate(request.getFollowUpDate());
        if (request.getStatus() != null)
            record.setStatus(MedicalRecord.RecordStatus
                    .valueOf(request.getStatus()));

        medicalRecordRepository.save(record);
        return MedicalRecordResponse.from(record);
    }
}
