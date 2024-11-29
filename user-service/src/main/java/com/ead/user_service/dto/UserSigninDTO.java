package com.ead.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSigninDTO {
    private String token;
    private UserDTO user;
}
