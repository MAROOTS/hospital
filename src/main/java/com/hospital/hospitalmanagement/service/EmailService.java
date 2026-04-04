package com.hospital.hospitalmanagement.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("hh:mm a");

    @Async
    public void sendWelcomeEmail(String to, String name) {
        Context context = new Context();
        context.setVariable("name", name);
        sendEmail(to, "Welcome",
                "emails/welcome", context);
    }
    @Async
    public void sendAppointmentBookedEmail(String to, String patientName,
                                           String doctorName, String specialization,
                                           LocalDate date, LocalTime time, String type) {
        Context context = new Context();
        context.setVariable("patientName", patientName);
        context.setVariable("doctorName", doctorName);
        context.setVariable("specialization", specialization);
        context.setVariable("date", date.format(DATE_FORMATTER));
        context.setVariable("time", time.format(TIME_FORMATTER));
        context.setVariable("type", type.replace("_", " "));
        sendEmail(to, "Appointment Booked Successfully yoh",
                "emails/appointment-booked", context);
    }

    @Async
    public void sendAppointmentConfirmedEmail(String to, String patientName,
                                              String doctorName, LocalDate date, LocalTime time) {
        Context context = new Context();
        context.setVariable("patientName", patientName);
        context.setVariable("doctorName", doctorName);
        context.setVariable("date", date.format(DATE_FORMATTER));
        context.setVariable("time", time.format(TIME_FORMATTER));
        sendEmail(to, "Appointment Confirmed bro",
                "emails/appointment-confirmed", context);
    }
    @Async
    public void sendAppointmentCancelledEmail(String to, String patientName,
                                              String doctorName, LocalDate date,
                                              LocalTime time, String reason) {
        Context context = new Context();
        context.setVariable("patientName", patientName);
        context.setVariable("doctorName", doctorName);
        context.setVariable("date", date.format(DATE_FORMATTER));
        context.setVariable("time", time.format(TIME_FORMATTER));
        context.setVariable("reason",
                reason != null ? reason : "No reason provided");
        sendEmail(to, "Appointment Cancelled",
                "emails/appointment-cancelled", context);
    }
    @Async
    public void sendInvoiceEmail(String to, String patientName,
                                 String invoiceNumber, Double totalAmount,
                                 String dueDate, String status) {
        Context context = new Context();
        context.setVariable("patientName", patientName);
        context.setVariable("invoiceNumber", invoiceNumber);
        context.setVariable("totalAmount",
                String.format("KES %.2f", totalAmount));
        context.setVariable("dueDate", dueDate);
        context.setVariable("status", status);
        sendEmail(to, "Invoice " + invoiceNumber + " 🧾",
                "emails/invoice", context);
    }

    private void sendEmail(String to, String subject,
                           String template, Context context) {
        try {
            String html = templateEngine.process(template, context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Email sent to {} - {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}",
                    to, e.getMessage());
        }
    }
}
