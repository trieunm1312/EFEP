package com.team1.efep.controllers;

import com.team1.efep.configurations.AllPage;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public void googleLogin(HttpServletResponse response) {
        accountService.getGoogleLoginUrl(response);
    }

    @GetMapping("/login/google/info")
    @Operation(hidden = true)
    public String getGoogleInfo(@RequestParam(name = "code") String code, Model model, HttpSession session) {
        return accountService.exchangeGoogleCode(code, model, session);
    }

    @GetMapping("/view/profile")
    @Operation(hidden = true)
    public String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        return accountService.viewProfile(session, model, redirectAttributes);
    }

    @GetMapping("/view/profile/api")
    public ViewProfileResponse viewProfile(@RequestBody ViewProfileRequest request) {
        return accountService.viewProfileAPI(request);
    }

    @PutMapping("/update/profile")
    @Operation(hidden = true)
    public String updateProfile(UpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        return accountService.updateProfile(request, session, redirectAttributes);
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
