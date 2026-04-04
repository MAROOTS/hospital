package com.hospital.hospitalmanagement.dto.request;
import com.hospital.hospitalmanagement.model.Invoice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Payment method is required")
    private Invoice.PaymentMethod paymentMethod;
    private Double amountPaid;
    private String transactionReference;
}
