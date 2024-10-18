package com.team1.efep.utils;

import com.team1.efep.services.AccountService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleLoginUtil {
    public static String generateGoogleLoginUrl() {
        String clientId = "660098983556-fc8ls122j77h1fongo4gj6dgk4plak7u.apps.googleusercontent.com";
        String scopes = "openid email profile";
        String redirectUri = "http://localhost:8080/account/login/google/info";

        return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&scope=" + scopes +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&access_type=offline";
    }

    public static void accessGoogleInfo(String accessToken, AccountService accountService) {
        String apiUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            String response = responseBuilder.toString();
            System.out.println("Response: " + response);


            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
