package ru.baskaeva.dressshopfront.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;

    private Long timestamp;
    private List<ProductHistory> products;

    private String deliveryAddress;
    private LocalDateTime dateTime;
    private Long totalCost;
    private Status status;

    public enum Status {
        InProgress,
        Cancelled,
        Delivery
    }
}
