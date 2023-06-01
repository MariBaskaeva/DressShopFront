package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductColor {
    private Long id;
    private String color;
    private String image;
    private Long price;
    private Boolean isLiked;
    List<ProductSize> sizes;
}
