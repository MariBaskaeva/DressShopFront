package ru.baskaeva.dressshopfront.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BagTotalDTO {
    private int totalCount;
    private long totalPrice;
    private long deliveryCost;
    private long totalCost;

}
