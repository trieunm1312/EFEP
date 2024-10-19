package com.team1.efep.utils;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.services.AccountService;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public static void accessGoogleInfo(
            String accessToken
//            AccountService accountService,
//            Model model,
//            AccountRepo accountRepo
    ) {
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
            /*Account account = accountRepo.findByEmail()
            RegisterRequest request = RegisterRequest.builder()
                    .name()
                    .email()
                    .phone()
                    .avatar()
                    .background()
                    .password()
                    .confirmPassword()
                    .build();
            accountService.register()*/

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractResponse(String response) {
        Map<String, String> result = new HashMap<>();
        response = response.replaceAll("[{}]", "");
        return response;
    }

    public static void main(String[] args) {
        System.out.println(extractResponse("{  \"id\": \"115608599751869845312\",\"email\": \"quynhpvnse182895@fpt.edu.vn\",\"verified_email\": true,\"name\": \"Pham Van Nhu Quynh (K18 HCM)\",\"given_name\": \"Pham Van Nhu Quynh\",\"family_name\": \"(K18 HCM)\",\"picture\":\"https://lh3.googleusercontent.com/a/ACg8ocLwU0Ca_5fgeXpcBYvQ8Kk5AsD21jqruI3n2ugrqYjSq4hp203S=s96-c\",\"hd\": \"fpt.edu.vn\"}"));
    }
}
