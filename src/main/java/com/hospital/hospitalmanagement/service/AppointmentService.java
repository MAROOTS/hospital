package com.hospital.hospitalmanagement.service;

import com.hospital.hospitalmanagement.dto.request.BookAppointmentRequest;
import com.hospital.hospitalmanagement.dto.request.UpdateAppointmentRequest;
import com.hospital.hospitalmanagement.dto.response.AppointmentResponse;
import com.hospital.hospitalmanagement.model.Appointment;
import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.model.Patient;
import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.AppointmentRepository;
import com.hospital.hospitalmanagement.repository.DoctorRepository;
import com.hospital.hospitalmanagement.repository.PatientRepository;
import com.hospital.hospitalmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public AppointmentResponse bookAppointment(String email, BookAppointmentRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(()->new RuntimeException("Please complete your patient profile first"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(()->new RuntimeException("Doc not found bro..."));

        if (doctor.getStatus()!=Doctor.DoctorStatus.ACTIVE){
            throw new RuntimeException("Doc is not available meehhn");
        }

        boolean slotTaken = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(
                doctor,
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                Appointment.AppointmentStatus.CANCELLED
        );
        if (slotTaken){
            throw new RuntimeException("This time slot is already booked. " +
                    "Please choose another time.");
        }
        String dayOfWeek = request.getAppointmentDate().getDayOfWeek().name();

        if (doctor.getAvailableDays()!=null &&
                !doctor.getAvailableDays().isEmpty() &&
                !doctor.getAvailableDays().contains(dayOfWeek)){
            throw new RuntimeException("Doctor is not available on " + dayOfWeek);
        }
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .type(request.getType())
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();

        appointmentRepository.save(appointment);
        return AppointmentResponse.from(appointment);

    }

    public List<AppointmentResponse> getMyAppointments(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepository
                .findByPatientOrderByAppointmentDateDesc(patient)
                .stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }

    //Doctor
    public List<AppointmentResponse> getDoctorAppointments(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return appointmentRepository
                .findByDoctorOrderByAppointmentDateDesc(doctor)
                .stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }

    //Doctor
    public List<AppointmentResponse> getTodaysAppointments(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return appointmentRepository
                .findTodaysAppointments(doctor, LocalDate.now())
                .stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String id) {
        return AppointmentResponse.from(
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found")));
    }

    public List<String> getAvailableSlots(String doctorId,
                                          LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> booked = appointmentRepository
                .findByAppointmentDateAndDoctor(date, doctor);

        List<String> bookedTimes = booked.stream()
                .filter(a -> a.getStatus() !=
                        Appointment.AppointmentStatus.CANCELLED)
                .map(a -> a.getAppointmentTime().toString())
                .toList();

        // Generate all slots based on doctor's schedule
        List<String> allSlots = new java.util.ArrayList<>();
        if (doctor.getStartTime() != null && doctor.getEndTime() != null) {
            java.time.LocalTime current = doctor.getStartTime();
            while (current.isBefore(doctor.getEndTime())) {
                allSlots.add(current.toString());
                current = current.plusMinutes(
                        doctor.getSlotDuration() != null ?
                                doctor.getSlotDuration() : 30);
            }
        }

        // Remove booked slots
        allSlots.removeAll(bookedTimes);
        return allSlots;
    }

    @Transactional
    public AppointmentResponse updateAppointment(String id,
                                                 UpdateAppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        if (request.getAppointmentDate() != null)
            appointment.setAppointmentDate(request.getAppointmentDate());
        if (request.getAppointmentTime() != null)
            appointment.setAppointmentTime(request.getAppointmentTime());
        if (request.getStatus() != null)
            appointment.setStatus(request.getStatus());
        if (request.getType() != null)
            appointment.setType(request.getType());
        if (request.getReason() != null)
            appointment.setReason(request.getReason());
        if (request.getNotes() != null)
            appointment.setNotes(request.getNotes());
        if (request.getCancellationReason() != null)
            appointment.setCancellationReason(request.getCancellationReason());

        appointmentRepository.save(appointment);
        return AppointmentResponse.from(appointment);
    }

    @Transactional
    public AppointmentResponse cancelAppointment(String id,
                                                 String reason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        if (appointment.getStatus() ==
                Appointment.AppointmentStatus.COMPLETED) {
            throw new RuntimeException(
                    "Cannot cancel a completed appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        appointmentRepository.save(appointment);
        return AppointmentResponse.from(appointment);
    }
}
