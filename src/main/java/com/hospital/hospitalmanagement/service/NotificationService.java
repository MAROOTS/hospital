package com.hospital.hospitalmanagement.service;
import com.hospital.hospitalmanagement.dto.response.NotificationResponse;
import com.hospital.hospitalmanagement.model.Notification;
import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.NotificationRepository;
import com.hospital.hospitalmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    //Send notification to a specific user
    @Transactional
    public void sendNotification(User recipient, String title,
                                 String message, Notification.NotificationType type,
                                 String referenceId, String referenceType) {

        // Save to database
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .build();

        notificationRepository.save(notification);

        // Send via WebSocket to specific user
        NotificationResponse response =
                NotificationResponse.from(notification);

        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(),
                "/queue/notifications",
                response
        );

        log.info("Notification sent to {}: {}",
                recipient.getEmail(), title);
    }



    public void notifyAppointmentBooked(User doctor,
                                        String patientName, String date, String time,
                                        String appointmentId) {
        sendNotification(
                doctor,
                "New Appointment Booked",
                patientName + " booked an appointment on " +
                        date + " at " + time,
                Notification.NotificationType.APPOINTMENT_BOOKED,
                appointmentId, "APPOINTMENT"
        );
    }

    public void notifyAppointmentConfirmed(User patient,
                                           String doctorName, String date, String time,
                                           String appointmentId) {
        sendNotification(
                patient,
                "Appointment Confirmed",
                "Your appointment with Dr. " + doctorName +
                        " on " + date + " at " + time + " is confirmed",
                Notification.NotificationType.APPOINTMENT_CONFIRMED,
                appointmentId, "APPOINTMENT"
        );
    }

    public void notifyAppointmentCancelled(User recipient,
                                           String details, String appointmentId) {
        sendNotification(
                recipient,
                "Appointment Cancelled ",
                details,
                Notification.NotificationType.APPOINTMENT_CANCELLED,
                appointmentId, "APPOINTMENT"
        );
    }

    public void notifyMedicalRecordCreated(User patient,
                                           String doctorName, String diagnosis,
                                           String recordId) {
        sendNotification(
                patient,
                "New Medical Record Added",
                "Dr. " + doctorName + " added a medical record: " +
                        diagnosis,
                Notification.NotificationType.MEDICAL_RECORD_CREATED,
                recordId, "MEDICAL_RECORD"
        );
    }

    public void notifyInvoiceCreated(User patient,
                                     String invoiceNumber, String amount,
                                     String invoiceId) {
        sendNotification(
                patient,
                "New Invoice Generated",
                "Invoice " + invoiceNumber + " for " + amount +
                        " has been generated",
                Notification.NotificationType.INVOICE_CREATED,
                invoiceId, "INVOICE"
        );
    }

    public void notifyInvoicePaid(User recipient,
                                  String invoiceNumber, String invoiceId) {
        sendNotification(
                recipient,
                "Payment Received",
                "Invoice " + invoiceNumber + " has been paid",
                Notification.NotificationType.INVOICE_PAID,
                invoiceId, "INVOICE"
        );
    }

    public void notifyNewPatientRegistered(String adminEmail,
                                           String patientName) {
        userRepository.findByEmail(adminEmail).ifPresent(admin ->
                sendNotification(
                        admin,
                        "New Patient Registered",
                        patientName + " has registered as a new patient",
                        Notification.NotificationType.NEW_PATIENT_REGISTERED,
                        null, null
                )
        );
    }

    //Get notifications for a user
    public List<NotificationResponse> getMyNotifications(
            String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        return notificationRepository
                .findByRecipientOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    //Get unread count
    public long getUnreadCount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        return notificationRepository
                .countByRecipientAndReadFalse(user);
    }

    //Mark all as read
    @Transactional
    public void markAllAsRead(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        notificationRepository.markAllAsRead(user);
    }

    //Mark single as read
    @Transactional
    public void markAsRead(String id) {
        notificationRepository.markAsRead(id);
    }
}
