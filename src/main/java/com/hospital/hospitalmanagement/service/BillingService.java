package com.hospital.hospitalmanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.hospitalmanagement.dto.InvoiceItem;
import com.hospital.hospitalmanagement.dto.request.CreateInvoiceRequest;
import com.hospital.hospitalmanagement.dto.request.PaymentRequest;
import com.hospital.hospitalmanagement.dto.response.InvoiceResponse;
import com.hospital.hospitalmanagement.model.*;
import com.hospital.hospitalmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request){
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(()->new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(()->new RuntimeException("Doc not found my guy"));

        Appointment appointment =null;
        if (request.getAppointmentId()!=null){
            appointment = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        }
        List<InvoiceItem> items = request.getItems();
        items.forEach(item->item.setTotal(item.getQuantity() * item.getUnitPrice()));
        double subtotal = items.stream()
                .mapToDouble(InvoiceItem::getTotal)
                .sum();


        String itemsJson;
        try {
            itemsJson= objectMapper.writeValueAsString(items);
        }catch (Exception e){
            throw new RuntimeException("Failed to process invoice items");
        }
        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .items(itemsJson)
                .subtotal(subtotal)
                .discount(request.getDiscount() != null ?
                        request.getDiscount() : 0.0)
                .dueDate(request.getDueDate())
                .notes(request.getNotes())
                .build();

        invoiceRepository.save(invoice);
        return InvoiceResponse.from(invoice);
    }
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(InvoiceResponse::from)
                .collect(Collectors.toList());
    }
    public InvoiceResponse getInvoiceById(String id) {
        return InvoiceResponse.from(
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Invoice not found")));
    }

    public List<InvoiceResponse> getMyInvoices(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found bro...."));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(()->new RuntimeException("Patient not found...."));
        return invoiceRepository.findByPatientOrderByCreatedAtDesc(patient)
                .stream()
                .map(InvoiceResponse::from)
                .collect(Collectors.toList());
    }
    public List<InvoiceResponse> getUnpaidInvoices() {
        return invoiceRepository.findByStatus(Invoice.PaymentStatus.UNPAID)
                .stream()
                .map(InvoiceResponse::from)
                .collect(Collectors.toList());
    }
    @Transactional
    public InvoiceResponse processPayment(String invoiceId, PaymentRequest request){
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new RuntimeException("Invoice not found"));
        if (invoice.getStatus()==Invoice.PaymentStatus.PAID){
            throw new RuntimeException("This invoice is already paid bro..");
        }
        if (invoice.getStatus()==Invoice.PaymentStatus.CANCELLED){
            throw new RuntimeException("Cannot pay a cancelled invoice");
        }
        if (request.getAmountPaid() != null &&
                request.getAmountPaid() < invoice.getTotalAmount()) {
            invoice.setStatus(Invoice.PaymentStatus.PARTIAL);
        } else {
            invoice.setStatus(Invoice.PaymentStatus.PAID);
            invoice.setPaidDate(LocalDate.now());
        }

        invoice.setPaymentMethod(request.getPaymentMethod());
        invoiceRepository.save(invoice);
        return InvoiceResponse.from(invoice);
    }
    @Transactional
    public InvoiceResponse cancelInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (invoice.getStatus() == Invoice.PaymentStatus.PAID) {
            throw new RuntimeException("Cannot cancel a paid invoice");
        }

        invoice.setStatus(Invoice.PaymentStatus.CANCELLED);
        invoiceRepository.save(invoice);
        return InvoiceResponse.from(invoice);
    }
    public BillingStatsResponse getBillingStats() {
        return new BillingStatsResponse(
                invoiceRepository.getTotalRevenue(),
                invoiceRepository.getTotalOutstanding(),
                invoiceRepository.countByStatus(Invoice.PaymentStatus.PAID),
                invoiceRepository.countByStatus(Invoice.PaymentStatus.UNPAID),
                invoiceRepository.countByStatus(Invoice.PaymentStatus.OVERDUE)
        );
    }
    public Double getRevenueByDateRange(LocalDate start, LocalDate end){
        return invoiceRepository.getRevenueByDateRange(start,end);
    }
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void markOverdueInvoices(){
        List<Invoice> overdue = invoiceRepository.findOverdueInvoices(LocalDate.now());
        overdue.forEach(invoice -> invoice.setStatus(Invoice.PaymentStatus.OVERDUE));
        invoiceRepository.saveAll(overdue);
        log.info("Marked {} invoices as overdue", overdue.size());
    }
    private String generateInvoiceNumber(){
        String number;
        do {
            number = "INV-" + LocalDate.now().getYear() + "-" +
                    String.format("%06d", new Random().nextInt(999999));
        }while (invoiceRepository.existsByInvoiceNumber(number));{
            return number;
        }
    }
    public record BillingStatsResponse(
            Double totalRevenue,
            Double totalOutstanding,
            long totalPaid,
            long totalUnpaid,
            long totalOverdue) {}
}
