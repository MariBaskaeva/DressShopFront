package ru.baskaeva.dressshopfront.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistory {
    private Long id;

    private ProductInfo productInfo;

    private String color;
    private String image;
    private Long price;
    private ProductSize.Size size;
    private Integer count;
}

