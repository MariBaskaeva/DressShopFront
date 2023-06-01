package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductsPageableDTO {
    private List<ProductDTO> content;
    private Pageable pageable;
    private Boolean last;
    private Integer totalElements;
    private Integer totalPages;
    private Integer size;
    private Integer number;
    private Sort sort;
    private Boolean first;
    private Integer numberOfElements;
    private Boolean empty;
}

@Data
class ProductsList {
    private List<ProductDTO> products = new ArrayList<>();
}

@Data
class Sort {
    private Boolean empty;
    private Boolean sorted;
    private Boolean unsorted;
}
