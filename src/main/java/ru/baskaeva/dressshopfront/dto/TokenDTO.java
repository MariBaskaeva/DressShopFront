package ru.baskaeva.dressshopfront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {
    @JsonProperty("auth-token")
    String authToken;
}
