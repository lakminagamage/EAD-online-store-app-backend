package com.ead.user_service.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String type;
    private String name;
    private String email;
    private String country;
    private String phone;
    private int postalCode;
    private String password;
}
