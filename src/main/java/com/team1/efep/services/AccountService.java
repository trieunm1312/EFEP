package com.team1.efep.services;

import com.team1.efep.models.request_models.ChangePasswordRequest;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.request_models.UpdateProfileRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AccountService {
    String register(RegisterRequest request, Model model, RedirectAttributes redirectAttributes);

    String login(LoginRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes);

    void getGoogleLoginUrl(HttpServletResponse response);

    String exchangeGoogleCode(String code, Model model,HttpSession session, RedirectAttributes redirectAttributes);

    String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String updateProfile(UpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes);

    String changePassword(ChangePasswordRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String logout(HttpSession session);
}
