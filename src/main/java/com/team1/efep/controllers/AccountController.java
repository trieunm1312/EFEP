package com.team1.efep.controllers;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "Account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(hidden = true)
    public String register(RegisterRequest request, Model model) {
        return accountService.register(request, model);
    }

    @PostMapping("/register/api")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return accountService.registerAPI(request);
    }

    @PostMapping("/login")
    @Operation(hidden = true)
    public String login(LoginRequest request, Model model, HttpSession session) {
        return accountService.login(request, model, session);
    }

    @PostMapping("/login/api")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return accountService.loginAPI(request);
    }

    @GetMapping("/login/google")
    public LoginGoogleResponse googleLogin() {
        return accountService.getGoogleLoginUrl();
    }

    @GetMapping("/login/google/info")
    @Operation(hidden = true)
    public void getGoogleInfo(@RequestParam(name = "code") String code) {
        accountService.exchangeGoogleCode(code);
    }

    @GetMapping("/view/profile")
    @Operation(hidden = true)
    public String viewProfile(@ModelAttribute ViewProfileRequest request, HttpSession session, Model model) {
        return accountService.viewProfile(request, session, model);
    }

    @GetMapping("/view/profile/api")
    public ViewProfileResponse viewProfile(@RequestBody ViewProfileRequest request) {
        return accountService.viewProfileAPI(request);
    }

    @PutMapping("/update/profile")
    @Operation(hidden = true)
    public String updateProfile(UpdateProfileRequest request, HttpSession session, Model model) {
        return accountService.updateProfile(request, session, model);
    }

    @PutMapping("/update/profile/api")
    public UpdateProfileResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        return accountService.updateProfileAPI(request);
    }

    @PostMapping("/change/password")
    @Operation(hidden = true)
    public String changePassword(ChangePasswordRequest request, HttpSession session, Model model) {
        return accountService.changePassword(request, session, model);
    }

    @PostMapping("/change/password/api")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {
        return accountService.changePasswordAPI(request);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        return accountService.logout(session);
    }

}
