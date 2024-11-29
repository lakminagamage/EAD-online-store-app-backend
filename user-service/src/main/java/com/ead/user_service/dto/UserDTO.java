package com.ead.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String type;
    private String name;
    private String email;
    private String country;
    private String phone;
    private int postalCode;
    private String createdAt;
    private String updatedAt;
}