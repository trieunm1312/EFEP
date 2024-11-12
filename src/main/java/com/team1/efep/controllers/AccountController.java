package com.team1.efep.controllers;

import com.team1.efep.models.request_models.ChangePasswordRequest;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.request_models.UpdateProfileRequest;
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
    public String register(RegisterRequest request, Model model, RedirectAttributes redirectAttributes) {
        return accountService.register(request, model, redirectAttributes);
    }

    @PostMapping("/login")
    @Operation(hidden = true)
    public String login(LoginRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        return accountService.login(request, model, session, redirectAttributes);
    }

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) {
        accountService.getGoogleLoginUrl(response);
    }

    @GetMapping("/login/google/info")
    @Operation(hidden = true)
    public String getGoogleInfo(@RequestParam(name = "code") String code, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        return accountService.exchangeGoogleCode(code, model, session, redirectAttributes);
    }

    @GetMapping("/view/profile")
    @Operation(hidden = true)
    public String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return accountService.viewProfile(session, model, redirectAttributes);
    }

    @PutMapping("/update/profile")
    @Operation(hidden = true)
    public String updateProfile(UpdateProfileRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        return accountService.updateProfile(request, session, redirectAttributes);
    }

    @PostMapping("/change/password")
    @Operation(hidden = true)
    public String changePassword(ChangePasswordRequest request, HttpSession session, Model model,RedirectAttributes redirectAttributes) {
        return accountService.changePassword(request, session, model, redirectAttributes);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        return accountService.logout(session);
    }

}
