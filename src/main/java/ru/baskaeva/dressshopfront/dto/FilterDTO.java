package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {
    private List<String> colors;
    private List<ProductInfo.Type> types;
    private List<ProductSize.Size> sizes;
    private Long priceFrom;
    private Long priceTo;
    private Boolean isLiked;
}
