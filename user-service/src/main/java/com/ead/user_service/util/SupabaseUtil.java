package com.ead.user_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SupabaseUtil {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> signUp(String email, String password) {
        String url = supabaseUrl + "/auth/v1/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> signIn(String email, String password) {
        String url = supabaseUrl + "/auth/v1/token?grant_type=password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public boolean verifyToken(String token) {
        String url = supabaseUrl + "/auth/v1/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public ResponseEntity<String> sendPasswordResetEmail(String email) {
        String url = supabaseUrl + "/auth/v1/recover";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public ResponseEntity<String> resetPassword(String accessToken, String newPassword) {
        String url = supabaseUrl + "/auth/v1/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + accessToken);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("password", newPassword);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<String> verifyOtp(String email, String type, String token) {
        String url = supabaseUrl + "/auth/v1/verify";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("type", type);
        requestBody.put("token", token);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }
}