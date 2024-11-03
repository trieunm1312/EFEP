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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/user/list")
    @Operation(hidden = true)
    public String viewUserList(HttpSession session, Model model) {
        return adminService.viewUserList(session, model);
    }

    @GetMapping("/user/list/api")
    public ViewUserListResponse viewUserList() {
        return adminService.viewUserListAPI();
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
    public String banUser(BanUserRequest request, Model model, RedirectAttributes redirectAttributes) {
        return adminService.banUser(request, model, redirectAttributes);
    }

    @PutMapping("/ban/user/api")
    public BanUserResponse banUser(@RequestBody BanUserRequest request) {
        return adminService.banUserAPI(request);
    }

    @PutMapping("/unban/user")
    @Operation(hidden = true)
    public String unBanUser(UnBanUserRequest request, Model model, RedirectAttributes redirectAttributes) {
        return adminService.unBanUser(request, model, redirectAttributes);
    }

    @PutMapping("/unban/user/api")
    public UnBanUserResponse unBanUser(@RequestBody UnBanUserRequest request) {
        return adminService.unBanUserAPI(request);
    }

    @PostMapping("/account/seller")
    @Operation(hidden = true)
    public String createAccountForSeller(CreateAccountForSellerRequest request, Model model,  RedirectAttributes redirectAttributes) {
        return adminService.createAccountForSeller(request, model, redirectAttributes);
    }

    @PostMapping("/account/seller/api")
    public CreateAccountForSellerResponse createAccountForSeller(@RequestBody CreateAccountForSellerRequest request) {
        return adminService.createAccountForSellerAPI(request);
    }

}
