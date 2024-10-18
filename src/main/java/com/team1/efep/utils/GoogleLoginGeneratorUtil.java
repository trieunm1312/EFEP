package com.team1.efep.utils;

import com.team1.efep.pojo.GoogleToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class GoogleLoginGeneratorUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleLoginGeneratorUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public GoogleToken exchangeAuthorizationCode(String authorizationCode) {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";
        String clientId = "660098983556-fc8ls122j77h1fongo4gj6dgk4plak7u.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/account/login/google/info";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("code", authorizationCode);
        requestBody.put("redirect_uri", redirectUri);

        HttpEntity<String> requestEntity = new HttpEntity<>(toUrlEncodedString(requestBody), headers);

        ResponseEntity<GoogleToken> responseEntity = restTemplate.postForEntity(
                URI.create(tokenEndpoint), requestEntity, GoogleToken.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to exchange authorization code for access token");
        }
    }

    private String toUrlEncodedString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
