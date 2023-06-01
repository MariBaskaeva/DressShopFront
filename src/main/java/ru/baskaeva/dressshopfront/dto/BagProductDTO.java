package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BagProductDTO {
    private Long bagId;
    private Long productId;
    private String title;
    private ProductInfo.Type type;
    private String color;
    private String image;
    private Long price;
    private ProductSize.Size size;
    private Integer count;
    private Integer countInBag;
}
