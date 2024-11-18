package com.team1.efep.controllers;

import com.team1.efep.configurations.AdminPageConfig;
import com.team1.efep.configurations.AllPage;
import com.team1.efep.configurations.HomepageConfig;
import com.team1.efep.configurations.SellerPageConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Category;
import com.team1.efep.models.request_models.FilterCategoryRequest;
import com.team1.efep.models.response_models.AddToWishlistResponse;
import com.team1.efep.models.response_models.FilterCategoryResponse;
import com.team1.efep.models.response_models.UpdateProfileResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.services.AdminService;
import com.team1.efep.services.BuyerService;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.OutputCheckerUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BuyerService buyerService;

    private final SellerService sellerService;

    private final AdminService adminService;

    private final AccountRepo accountRepo;

    @GetMapping("/")
    public String startPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            AllPage.allConfig(model, buyerService);
            HomepageConfig.config(model,buyerService);
            return "home";
        }
        Role.changeToBuyer(account, accountRepo);
        AllPage.allConfig(model, buyerService);
        HomepageConfig.config(model,buyerService);
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
    public String manageFlowerPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        sellerService.viewFlowerListForSeller(session, model, redirectAttributes);
        return "manageFlower";
    }

    @GetMapping("/viewOrderSummary")
    public String viewOrderSummaryPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "viewOrderSummary";
    }

    @GetMapping("/seller/plan")
    public String myPlanPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "feedback";
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
    public String sellerDashboard(Model model, HttpSession session) {
        SellerPageConfig.config(model, sellerService, session);
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
            return "redirect:/account/view/profile";
        }

        return "sellerProfile";
    }

    @GetMapping("/myAccount")
    public String myAccountPage(Model model,  RedirectAttributes redirectAttributes) {

        if(model.getAttribute("msg") != null) {
            if (OutputCheckerUtil.checkIfThisIsAResponseObject(model.getAttribute("msg"), UpdateProfileResponse.class)) {

                model.addAttribute("msg", (UpdateProfileResponse)model.getAttribute("msg"));
            } else  {
                model.addAttribute("msg", (ViewProfileResponse)model.getAttribute("msg"));
            }

        } else  {
            redirectAttributes.addFlashAttribute("error", model.getAttribute("error"));
            return "redirect:/account/view/profile";
        }

        AllPage.allConfig(model, buyerService);
        return "myAccount";
    }


    @GetMapping("/wishlist")
    public String viewWishlistPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "viewWishlist";
    }

    @GetMapping("/category")
    public String categoryPage(Model model, RedirectAttributes redirectAttributes) {
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(model.getAttribute("msg"), AddToWishlistResponse.class)){
            int categoryId = Integer.parseInt(((AddToWishlistResponse)model.getAttribute("msg")).getKeyword());
            return buyerService.filterCategory(FilterCategoryRequest.builder().categoryId(categoryId).build(), redirectAttributes);
        }
        model.addAttribute("msg", (FilterCategoryResponse)model.getAttribute("msg"));
        AllPage.allConfig(model, buyerService);
        return "category";
    }

    @GetMapping("/about/us")
    public String aboutUsPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "aboutUs";
    }

    @GetMapping("/privacy/policy")
    public String privacyPolicyPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "privacyPolicy";
    }

    @GetMapping("/terms/conditions")
    public String termConditionPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "termCondition";
    }

    @GetMapping("/password/renew")
    public String renewPasswordPage(Model model) {
        AllPage.allConfig(model, buyerService);
        return "renewPassword";
    }

}



