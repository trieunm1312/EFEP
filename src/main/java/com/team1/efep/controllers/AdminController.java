package com.team1.efep.controllers;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.AdminService;
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
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/plan")
    @Operation(hidden = true)
    public String createBusinessPlan(CreateBusinessPlanRequest request, Model model) {
        return adminService.createBusinessPlan(request, model);
    }

    @PostMapping("/plan/api")
    public CreateBusinessPlanResponse createBusinessPlan(@RequestBody CreateBusinessPlanRequest request) {
        return adminService.createBusinessPlanAPI(request);
    }

    @PutMapping("/plan")
    @Operation(hidden = true)
    public String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model) {
        return adminService.updateBusinessPlan(request, model);
    }

    @PutMapping("/plan/api")
    public UpdateBusinessPlanResponse updateBusinessPlan(@RequestBody UpdateBusinessPlanRequest request) {
        return adminService.updateBusinessPlanAPI(request);
    }

    @DeleteMapping("/plan")
    @Operation(hidden = true)
    public String disableBusinessPlan(DisableBusinessPlanRequest request, Model model) {
        return adminService.disableBusinessPlan(request, model);
    }

    @DeleteMapping("/plan/api")
    public DisableBusinessPlanResponse disableBusinessPlan(@RequestBody DisableBusinessPlanRequest request) {
        return adminService.disableBusinessPlanAPI(request);
    }

    @PostMapping("/service")
    @Operation(hidden = true)
    public String createBusinessService(CreateBusinessServiceRequest request, Model model) {
        return adminService.createBusinessService(request, model);
    }

    @PostMapping("/service/api")
    public CreateBusinessServiceResponse createBusinessService(@RequestBody CreateBusinessServiceRequest request) {
        return adminService.createBusinessServiceAPI(request);
    }

    @PutMapping("/service")
    @Operation(hidden = true)
    public String updateBusinessService(UpdateBusinessServiceRequest request, Model model) {
        return adminService.updateBusinessService(request, model);
    }

    @PutMapping("/service/api")
    public UpdateBusinessServiceResponse updateBusinessService(@RequestBody UpdateBusinessServiceRequest request) {
        return adminService.updateBusinessServiceAPI(request);
    }

    @DeleteMapping("/service")
    @Operation(hidden = true)
    public String deleteBusinessService(DeleteBusinessServiceRequest request, Model model) {
        return adminService.deleteBusinessService(request, model);
    }

    @DeleteMapping("/service/api")
    public DeleteBusinessServiceResponse deleteBusinessService(@RequestBody DeleteBusinessServiceRequest request) {
        return adminService.deleteBusinessServiceAPI(request);
    }

    @GetMapping("/user/list")
    @Operation(hidden = true)
    public String viewUserList(HttpSession session, Model model) {
        return adminService.viewUserList(session, model);
    }

    @GetMapping("/user/list/api")
    public ViewUserListResponse viewUserList() {
        return adminService.viewUserListAPI();
    }

    @GetMapping("/view/plan")
    @Operation(hidden = true)
    public String viewBusinessPlan(HttpSession session, Model model) {
        return adminService.viewBusinessPlan(session, model);
    }

    @GetMapping("/view/plan/api")
    public ViewBusinessPlanResponse viewBusinessPlan() {
        return adminService.viewBusinessPlanAPI();
    }

    @GetMapping("/view/service")
    @Operation(hidden = true)
    public String viewBusinessService(HttpSession session, Model model) {
        return adminService.viewBusinessService(session, model);
    }

    @GetMapping("/view/service/api")
    public ViewBusinessServiceResponse viewBusinessService() {
        return adminService.viewBusinessServiceAPI();
    }

    @PostMapping("/search/user/")
    public String searchUserList(HttpSession session, SearchUserListRequest request, Model model) {
        return adminService.searchUserList(session, request, model);
    }

    @PostMapping("/search/admin/api")
    public SearchUserListResponse searchUserList(@RequestBody SearchUserListRequest request) {
        return adminService.searchUserListAPI(request);
    }

    @PutMapping("/ban/user")
    @Operation(hidden = true)
    public String banUser(BanUserRequest request, Model model) {
        return adminService.banUser(request, model);
    }

    @PutMapping("/ban/user/api")
    public BanUserResponse banUser(@RequestBody BanUserRequest request) {
        return adminService.banUserAPI(request);
    }

    @PutMapping("/unban/user")
    @Operation(hidden = true)
    public String unBanUser(UnBanUserRequest request, Model model) {
        return adminService.unBanUser(request, model);
    }

    @PutMapping("/unban/user/api")
    public UnBanUserResponse unBanUser(@RequestBody UnBanUserRequest request) {
        return adminService.unBanUserAPI(request);
    }

    @PostMapping("/account/seller")
    @Operation(hidden = true)
    public String createAccountForSeller(CreateAccountForSellerRequest request, Model model) {
        return adminService.createAccountForSeller(request, model);
    }

    @PostMapping("/account/seller/api")
    public CreateAccountForSellerResponse createAccountForSeller(@RequestBody CreateAccountForSellerRequest request) {
        return adminService.createAccountForSellerAPI(request);
    }

}
