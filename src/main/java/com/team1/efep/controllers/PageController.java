package com.team1.efep.controllers;

import com.team1.efep.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BuyerService buyerService;

    @GetMapping("/")
    public String startPage(Model model) {
        buyerService.viewSlideBar(model);
        buyerService.viewFlowerTopList(10, model);
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/change/password")
    public String changePasswordPage() {
        return "changePassword";
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordPage() {
        return "forgotPassword";
    }

    @GetMapping("/plan")
    public String planPage() {
        return "manageBusinessPlan";
    }

    @GetMapping("/service")
    public String servicePage() {
        return "manageBusinessService";
    }
}


