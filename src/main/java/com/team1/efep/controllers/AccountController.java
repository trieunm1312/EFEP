package com.team1.efep.controllers;

import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.response_models.LoginResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import com.team1.efep.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(hidden = true)
    public String register(@RequestBody RegisterRequest request, Model model) {
        return accountService.register(request, model);
    }

    @PostMapping("/register/api")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return accountService.registerAPI(request);
    }

    @PostMapping("/login")
    @Operation(hidden = true)
    public String login(@RequestBody LoginRequest request, Model model, HttpSession session){
        return accountService.login(request, model, session);
    }

    @PostMapping("/login/api")
    public LoginResponse login(@RequestBody LoginRequest request){
        return accountService.loginAPI(request);
    }
}
