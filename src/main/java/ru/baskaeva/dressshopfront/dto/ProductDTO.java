package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private ProductInfo info;
    private List<ProductColor> colors;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductInfo {
    private String title;
    private String vendorCode;
    private String description;
    private String manufacturer;
    private Type type;
    public enum Type{
        BLOUSE,
        TROUSERS,
        JACKET,
        COAT,
        DRESS,
        SKIRT
    }
}