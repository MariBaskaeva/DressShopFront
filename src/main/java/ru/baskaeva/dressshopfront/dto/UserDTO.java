package ru.baskaeva.dressshopfront.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String surname;
    private String name;
    private String phoneNumber;
    private String telegram;
    private String password;
    private String passwordAgain;
}