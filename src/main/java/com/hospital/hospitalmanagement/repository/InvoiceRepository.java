package com.hospital.hospitalmanagement.repository;
import com.hospital.hospitalmanagement.model.Invoice;
import com.hospital.hospitalmanagement.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice,String>{
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByPatientOrderByCreatedAtDesc(Patient patient);

    List<Invoice> findByStatus(Invoice.PaymentStatus status);

    List<Invoice> findByPatientAndStatus(
            Patient patient, Invoice.PaymentStatus status);

    boolean existsByInvoiceNumber(String invoiceNumber);

    // Revenue stats
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i " +
            "WHERE i.status = 'PAID'")
    Double getTotalRevenue();

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i " +
            "WHERE i.status = 'PAID' AND " +
            "i.paidDate BETWEEN :startDate AND :endDate")
    Double getRevenueByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i " +
            "WHERE i.status = 'UNPAID' OR i.status = 'OVERDUE'")
    Double getTotalOutstanding();

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    long countByStatus(@Param("status") Invoice.PaymentStatus status);

    // Get overdue invoices
    @Query("SELECT i FROM Invoice i WHERE " +
            "i.status = 'UNPAID' AND i.dueDate < :today")
    List<Invoice> findOverdueInvoices(@Param("today") LocalDate today);
}
