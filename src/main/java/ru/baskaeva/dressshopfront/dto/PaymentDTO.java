package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String cardNumber;
    private String cardDate;
    private String cardCode;
    private Long orderId;
}