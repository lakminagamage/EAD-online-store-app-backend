package com.ead.user_service.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String country;
    private int postalCode;
}
