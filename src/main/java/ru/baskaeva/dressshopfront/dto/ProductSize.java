package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSize {
    private Long id;
    private Size size;
    private Integer count;
    private Integer countInBag;

    public enum Size {
        XS,
        S,
        M,
        L,
        XL,
        XXL
    }
}
