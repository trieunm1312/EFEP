package com.team1.efep.services;

import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.response_models.LoginResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AccountService {
    String register(RegisterRequest request, Model model);

    RegisterResponse registerAPI(RegisterRequest request);

    String login(LoginRequest request, Model model, HttpSession session);

    LoginResponse loginAPI(LoginRequest request);

}
