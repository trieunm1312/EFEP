package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AccountService {
    String register(RegisterRequest request, Model model);

    RegisterResponse registerAPI(RegisterRequest request);

    String login(LoginRequest request, Model model, HttpSession session);

    LoginResponse loginAPI(LoginRequest request);

    void getGoogleLoginUrl(HttpServletResponse response);

    String exchangeGoogleCode(String code, Model model,HttpSession session);

    String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes);

    ViewProfileResponse viewProfileAPI(ViewProfileRequest request);

    String updateProfile(UpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes);

    UpdateProfileResponse updateProfileAPI(UpdateProfileRequest request);

    String changePassword(ChangePasswordRequest request, HttpSession session, Model model);

    ChangePasswordResponse changePasswordAPI(ChangePasswordRequest request);

    String logout(HttpSession session);
}
