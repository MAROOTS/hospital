package com.hospital.hospitalmanagement.controller;
import com.hospital.hospitalmanagement.dto.request.CreateInvoiceRequest;
import com.hospital.hospitalmanagement.dto.request.PaymentRequest;
import com.hospital.hospitalmanagement.dto.response.InvoiceResponse;
import com.hospital.hospitalmanagement.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Billing", description = "Invoice and payment management")
@SecurityRequirement(name = "Bearer Authentication")
public class BillingController {
    private final BillingService billingService;

    @Operation(summary = "Create an invoice (Admin/Receptionist)")
    @PostMapping("/invoices")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<InvoiceResponse> createInvoice(
            @Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.ok(billingService.createInvoice(request));
    }

    @Operation(summary = "Get all invoices (Admin/Receptionist)")
    @GetMapping("/invoices")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(billingService.getAllInvoices());
    }

    @Operation(summary = "Get invoice by ID")
    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(
            @PathVariable String id) {
        return ResponseEntity.ok(billingService.getInvoiceById(id));
    }

    @Operation(summary = "Get my invoices (Patient)")
    @GetMapping("/my-invoices")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<InvoiceResponse>> getMyInvoices(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(billingService.getMyInvoices(email));
    }

    @Operation(summary = "Get all unpaid invoices")
    @GetMapping("/invoices/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<InvoiceResponse>> getUnpaidInvoices() {
        return ResponseEntity.ok(billingService.getUnpaidInvoices());
    }

    @Operation(summary = "Process payment for an invoice")
    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<InvoiceResponse> processPayment(
            @PathVariable String id,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(billingService.processPayment(id, request));
    }

    @Operation(summary = "Cancel an invoice")
    @PatchMapping("/invoices/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<InvoiceResponse> cancelInvoice(
            @PathVariable String id) {
        return ResponseEntity.ok(billingService.cancelInvoice(id));
    }

    @Operation(summary = "Get billing statistics")
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BillingService.BillingStatsResponse> getBillingStats() {
        return ResponseEntity.ok(billingService.getBillingStats());
    }

    @Operation(summary = "Get revenue by date range")
    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso =
                    DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso =
                    DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                billingService.getRevenueByDateRange(startDate, endDate));
    }

}
