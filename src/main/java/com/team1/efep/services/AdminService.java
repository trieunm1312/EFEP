package com.team1.efep.services;

import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import org.springframework.ui.Model;

public interface AdminService {

    String createBusinessPlan(CreateBusinessPlanRequest request, Model model);

    CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request);
}
