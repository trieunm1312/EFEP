package com.team1.efep.controllers;

import com.team1.efep.configurations.AdminPageConfig;
import com.team1.efep.configurations.AllPage;
import com.team1.efep.configurations.HomepageConfig;
import com.team1.efep.configurations.SellerPageConfig;
import com.team1.efep.models.response_models.FilterCategoryResponse;
import com.team1.efep.models.response_models.UpdateProfileResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
import com.team1.efep.services.AdminService;
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

    private final AdminService adminService;

    @GetMapping("/")
    public String startPage(Model model) {
        HomepageConfig.config(model,buyerService);
        AllPage.allConfig(model, buyerService);
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "register";
    }

    @GetMapping("/change/password")
    public String changePasswordPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "changePassword";
    }

    @GetMapping("/forgot/password")
    public String forgotPasswordPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "forgotPassword";
    }

    @GetMapping("/orderList")
    public String orderListPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "viewOrderList";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "checkout";
    }

    @GetMapping("/manageFlower")
    public String manageFlowerPage(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        sellerService.viewFlowerListForSeller(session, model);
        return "manageFlower";
    }

    @GetMapping("/viewOrderSummary")
    public String viewOrderSummaryPage(Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return "viewOrderSummary";
    }

    @GetMapping("/seller/plan")
    public String myPlanPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "sellerPlan";
    }

    @GetMapping("/seller/plan/intro")
    public String choosePlanPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "choosePlan";
    }

    @GetMapping("/planList")
    public String planListPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "planList";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboardPage(Model model) {
        AdminPageConfig.config(model, adminService);
        return "adminDashboard";
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(Model model) {
        SellerPageConfig.config(model, sellerService);
        return "sellerDashboard";
    }

    @GetMapping("/seller/profile")
    public String sellerProfilePage(Model model) {

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
    public String myAccountPage(Model model) {

        if(model.getAttribute("msg") != null) {
            if (OutputCheckerUtil.checkIfThisIsAResponseObject(model.getAttribute("msg"), UpdateProfileResponse.class)) {

                model.addAttribute("msg", (UpdateProfileResponse)model.getAttribute("msg"));
            } else  {
                model.addAttribute("msg", (ViewProfileResponse)model.getAttribute("msg"));
            }

        } else  {
            model.addAttribute("error",  (Map<String, String>) model.getAttribute("error"));
            return "redirect:/account/view/profile";
        }

//        model.addAttribute("msg", (ViewProfileResponse) model.getAttribute("msg"));
        AllPage.allConfig(model, buyerService);

        return "myAccount";
    }


    @GetMapping("/wishlist")
    public String viewWishlistPage(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return "viewWishlist";
    }

    @GetMapping("/category")
    public String categoryPage(Model model) {
        model.addAttribute("msg", (FilterCategoryResponse)model.getAttribute("msg"));
        AllPage.allConfig(model, buyerService);
        return "category";
    }

}



