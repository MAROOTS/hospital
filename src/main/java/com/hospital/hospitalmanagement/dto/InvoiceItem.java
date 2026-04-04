package com.hospital.hospitalmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem {
    private String description;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
}
