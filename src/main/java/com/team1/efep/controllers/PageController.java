package com.team1.efep.controllers;

import com.team1.efep.configurations.HomepageConfig;
import com.team1.efep.services.BuyerService;
import jakarta.servlet.http.HttpSession;
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
        HomepageConfig.config(model,buyerService);
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

    @GetMapping("/orderList")
    public String orderListPage() {
        return "viewOrderList";
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("/manageFlower")
    public String manageFlowerPage() {
        return "manageFlower";
    }

    @GetMapping("/viewOrderSummary")
    public String viewOrderSummaryPage(Model model, HttpSession session) {
        return "viewOrderSummary";
    }

    @GetMapping("/seller/plan")
    public String myPlanPage() {
        return "sellerPlan";
    }

    @GetMapping("/seller/plan/intro")
    public String choosePlanPage() {
        return "choosePlan";
    }
}


