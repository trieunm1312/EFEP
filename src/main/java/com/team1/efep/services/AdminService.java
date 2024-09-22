package com.team1.efep.services;

import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.request_models.DisableBusinessPlanRequest;
import com.team1.efep.models.request_models.UpdateBusinessPlanRequest;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import com.team1.efep.models.response_models.DisableBusinessPlanResponse;
import com.team1.efep.models.response_models.UpdateBusinessPlanResponse;
import org.springframework.ui.Model;

public interface AdminService {

    String createBusinessPlan(CreateBusinessPlanRequest request, Model model);

    CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request);

    String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model);

    UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request);

    String disableBusinessPlan(DisableBusinessPlanRequest request, Model model);

    DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request);
}
