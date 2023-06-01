package ru.baskaeva.dressshopfront.dto;

import lombok.Data;

@Data
public class Pageable {
    private Sort sort;
    private Integer offset;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean unpaged;
    private Boolean paged;
}
