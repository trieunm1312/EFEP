package com.team1.efep.controllers;

import com.team1.efep.configurations.HomepageConfig;
import com.team1.efep.models.response_models.UpdateProfileResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
import com.team1.efep.services.BuyerService;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.OutputCheckerUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BuyerService buyerService;

    private final SellerService sellerService;

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
    public String manageFlowerPage(HttpSession session, Model model) {
        sellerService.viewFlowerListForSeller(session, model);
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

    @GetMapping("/planList")
    public String planListPage() {
        return "planList";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        return "adminDashboard";
    }

    @GetMapping("/seller/profile")
    public String sellerProfile(Model model) {

        if(model.getAttribute("msg") != null) {
            if (OutputCheckerUtil.checkIfThisIsAResponseObject(model.getAttribute("msg"), UpdateProfileResponse.class)) {
                model.addAttribute("msg", (UpdateProfileResponse)model.getAttribute("msg"));
            } else  {
                model.addAttribute("msg", (ViewProfileResponse)model.getAttribute("msg"));
            }

        } else  {
            model.addAttribute("error",  (Map<String, String>) model.getAttribute("error"));
        }

        return "sellerProfile";
    }

    @GetMapping("/myAccount")
    public String myAccount(Model model) {
        if(model.getAttribute("msg") != null) {
            if (OutputCheckerUtil.checkIfThisIsAResponseObject(model.getAttribute("msg"), UpdateProfileResponse.class)) {

                model.addAttribute("msg", (UpdateProfileResponse)model.getAttribute("msg"));
            } else  {
                model.addAttribute("msg", (ViewProfileResponse)model.getAttribute("msg"));
            }
        } else  {
            model.addAttribute("error",  (Map<String, String>) model.getAttribute("error"));
        }
//        model.addAttribute("msg", (ViewProfileResponse) model.getAttribute("msg"));
        return "myAccount";
    }
}


