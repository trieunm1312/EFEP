package com.team1.efep.controllers;

import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import com.team1.efep.services.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/business/plane/create")
    public String createBusinessPlan(CreateBusinessPlanRequest request, Model model) {
        return adminService.createBusinessPlan(request, model);
    }

    @PostMapping("/business/plane/create/api")
    public CreateBusinessPlanResponse createBusinessPlan(@RequestBody CreateBusinessPlanRequest request) {
        return adminService.createBusinessPlanAPI(request);
    }

}
