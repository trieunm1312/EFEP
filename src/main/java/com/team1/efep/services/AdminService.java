package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import org.springframework.ui.Model;

public interface AdminService {

    String createBusinessPlan(CreateBusinessPlanRequest request, Model model);

    CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request);

    String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model);

    UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request);

    String disableBusinessPlan(DisableBusinessPlanRequest request, Model model);

    DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request);

    String createBusinessService(CreateBusinessServiceRequest request, Model model);

    CreateBusinessServiceResponse createBusinessServiceAPI(CreateBusinessServiceRequest request);

    String updateBusinessService(UpdateBusinessServiceRequest request, Model model);

    UpdateBusinessServiceResponse updateBusinessServiceAPI(UpdateBusinessServiceRequest request);

    String deleteBusinessService(DeleteBusinessServiceRequest request, Model model);

    DeleteBusinessServiceResponse deleteBusinessServiceAPI(DeleteBusinessServiceRequest request);
}
