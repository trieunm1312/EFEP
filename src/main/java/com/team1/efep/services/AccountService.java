package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AccountService {
    String register(RegisterRequest request, Model model);

    RegisterResponse registerAPI(RegisterRequest request);

    String login(LoginRequest request, Model model, HttpSession session);

    LoginResponse loginAPI(LoginRequest request);

    LoginGoogleResponse getGoogleLoginUrl();

    void exchangeGoogleCode(String code);

    String viewProfile(ViewProfileRequest request, HttpSession session, Model model);

    ViewProfileResponse viewProfileAPI(ViewProfileRequest request);

    String updateProfile(UpdateProfileRequest request, HttpSession session, Model model);

    UpdateProfileResponse updateProfileAPI(UpdateProfileRequest request);

    String changePassword(ChangePasswordRequest request, HttpSession session, Model model);

    ChangePasswordResponse changePasswordAPI(ChangePasswordRequest request);

    String logout(HttpSession session);
}
