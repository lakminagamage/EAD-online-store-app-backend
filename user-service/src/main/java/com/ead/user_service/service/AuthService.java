package com.ead.user_service.service;

import com.ead.user_service.dto.UserCreateDTO;
import com.ead.user_service.dto.UserDTO;
import com.ead.user_service.dto.UserSigninDTO;
import com.ead.user_service.util.SupabaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthService {

    @Autowired
    private SupabaseUtil supabaseUtil;

    @Autowired
    private UserService userService;

    public UserDTO register(UserCreateDTO userCreateDTO) {
        String email = userCreateDTO.getEmail();
        String password = userCreateDTO.getPassword();
        ResponseEntity<String> response = supabaseUtil.signUp(email, password);
        if (response.getStatusCode().is2xxSuccessful()) {
            return userService.createUser(userCreateDTO);
        } else {
            System.out.println("Error: " + response.getBody());
            return null;
        }
    }

    public UserSigninDTO login(String email, String password) {
        ResponseEntity<String> response = supabaseUtil.signIn(email, password);
        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                String accessToken = responseBody.get("access_token").asText();

                UserDTO userDTO = userService.getUserByEmail(email);
                UserSigninDTO userSigninDTO = new UserSigninDTO(accessToken, userDTO);

                return userSigninDTO;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public boolean verifyToken(String token) {
        return supabaseUtil.verifyToken(token);
    }

    public ResponseEntity<String> sendPasswordResetEmail(String email) {
        return supabaseUtil.sendPasswordResetEmail(email);
    }

    public ResponseEntity<String> resetPassword(String accessToken, String newPassword) {
        return supabaseUtil.resetPassword(accessToken, newPassword);
    }

    public ResponseEntity<String> verifyOtp(String email, String type, String token) {
        return supabaseUtil.verifyOtp(email, type, token);
    }

    // private UserDTO mapToUserDTO(User user) {
    //     return new UserDTO(user.getId(), user.getType(), user.getName(), user.getEmail(),
    //             user.getCountry(), user.getPhone(), user.getPostalCode(),
    //             user.getCreatedAt().toString(), user.getUpdatedAt().toString());
    // }
}