package com.team1.efep.controllers;

import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.request_models.ViewProfileRequest;
import com.team1.efep.models.response_models.LoginGoogleResponse;
import com.team1.efep.models.response_models.LoginResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
import com.team1.efep.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "Account")
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

    @GetMapping("/login/google")
    public LoginGoogleResponse googleLogin(){
        return accountService.getGoogleLoginUrl();
    }

    @GetMapping("/login/google/info")
    @Operation(hidden = true)
    public void getGoogleInfo(@RequestParam(name = "code") String code){
        accountService.exchangeGoogleCode(code);
    }

    @GetMapping("/view/profile")
    @Operation(hidden = true)
    public String viewProfile(ViewProfileRequest request, Model model) {
        return accountService.viewProfile(request, model);
    }

    @GetMapping("/view/profile/api")
    public ViewProfileResponse viewProfile(@RequestBody ViewProfileRequest request) {
        return accountService.viewProfileAPI(request);
    }
}
