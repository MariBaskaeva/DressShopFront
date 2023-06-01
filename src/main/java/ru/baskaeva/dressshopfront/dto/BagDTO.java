package ru.baskaeva.dressshopfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BagDTO {
    List<BagProductDTO> bagProducts;
}
