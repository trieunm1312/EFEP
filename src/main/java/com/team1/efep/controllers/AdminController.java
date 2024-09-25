package com.team1.efep.controllers;

import com.team1.efep.models.request_models.ChangePasswordRequest;
import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.request_models.DisableBusinessPlanRequest;
import com.team1.efep.models.request_models.UpdateBusinessPlanRequest;
import com.team1.efep.models.response_models.ChangePasswordResponse;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import com.team1.efep.models.response_models.DisableBusinessPlanResponse;
import com.team1.efep.models.response_models.UpdateBusinessPlanResponse;
import com.team1.efep.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public String disableBusinessPlan(DisableBusinessPlanRequest request, Model model) {
        return adminService.disableBusinessPlan(request, model);
    }

    @DeleteMapping("/plan/api")
    public DisableBusinessPlanResponse disableBusinessPlan(@RequestBody DisableBusinessPlanRequest request) {
        return adminService.disableBusinessPlanAPI(request);
    }
}
