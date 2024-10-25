package com.team1.efep.utils;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.repositories.WishlistRepo;
import com.team1.efep.service_implementors.AccountServiceImpl;
import com.team1.efep.services.AccountService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public static String accessGoogleInfo(
            String accessToken,
            AccountServiceImpl accountService,
            Model model,
            AccountRepo accountRepo,
            HttpSession session,
            UserRepo userRepo,
            WishlistRepo wishlistRepo
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
            GoogleResponse googleResponse = extractResponse(response);
            Account account = accountRepo.findByEmail(googleResponse.getEmail()).orElse(null);
            if (account == null) {
                Account acc = accountRepo.save(
                        Account.builder()
                                .status(Status.ACCOUNT_STATUS_ACTIVE)
                                .email(googleResponse.getEmail())
                                .password(googleResponse.getId())
                                .role(Role.BUYER)
                                .build()
                );

                User user = userRepo.save(
                        User.builder()
                                .account(acc)
                                .name(googleResponse.getFamilyName())
                                .phone("")
                                .avatar(googleResponse.getPicture())
                                .background("")
                                .createdDate(LocalDate.now())
                                .build()
                );

                acc.setUser(user);
                accountRepo.save(acc);

                Wishlist wishlist = wishlistRepo.save(
                        Wishlist.builder()
                                .user(user)
                                .wishlistItemList(new ArrayList<>())
                                .build()
                );

                user.setWishlist(wishlist);
                userRepo.save(user);
            }
            reader.close();
            connection.disconnect();
            return accountService.login(LoginRequest.builder().email(googleResponse.getEmail()).password(googleResponse.getId()).build(), model, session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login";
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class GoogleResponse {
        String id;
        String email;
        String verifiedEmail;
        String name;
        String givenName;
        String familyName;
        String picture;
    }

    private static GoogleResponse extractResponse(String response) {
        String[] keyValuePairs = response.trim().replaceAll("[{}\"]", "").split(",");
        return GoogleResponse.builder()
                .id(getField(keyValuePairs, 0))
                .email(getField(keyValuePairs, 1))
                .verifiedEmail(getField(keyValuePairs, 2))
                .name(getField(keyValuePairs, 3))
                .givenName(getField(keyValuePairs, 4))
                .familyName(getField(keyValuePairs, 5))
                .picture(getField(keyValuePairs, 6))
                .build();
    }

    private static String getField(String[] keyValuePairs, int index) {
        return keyValuePairs[index].substring(keyValuePairs[index].indexOf(":") + 1).trim();
    }
}
