package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.BusinessPlan;
import com.team1.efep.models.entity_models.BusinessService;
import com.team1.efep.models.entity_models.PlanService;
import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.request_models.UpdateBusinessPlanRequest;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import com.team1.efep.models.response_models.UpdateBusinessPlanResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.BusinessPlanRepo;
import com.team1.efep.repositories.BusinessServiceRepo;
import com.team1.efep.repositories.PlanServiceRepo;
import com.team1.efep.services.AdminService;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.ChangePasswordValidation;
import com.team1.efep.validations.CreateBusinessPlanValidation;
import com.team1.efep.validations.UpdateBusinessPlanValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BusinessServiceRepo businessServiceRepo;

    private final BusinessPlanRepo businessPlanRepo;

    private final PlanServiceRepo planServiceRepo;
    private final AccountRepo accountRepo;

    //-------------------------------------CREATE BUSINESS PLAN------------------------------------------//
    @Override
    public String createBusinessPlan(CreateBusinessPlanRequest request, Model model) {
        Object output = createBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessPlanResponse.class)){
           model.addAttribute("msg", (CreateBusinessPlanResponse)output);
            return "home";
        }
        model.addAttribute("error", (Map<String, String>)output);
        return "home";
    }

    @Override
    public CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request) {
        Object output = createBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateBusinessPlanResponse.class)){
            return (CreateBusinessPlanResponse) output;
        }

        return CreateBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>)output))
                .build();
    }

    private Object createBusinessPlanLogic(CreateBusinessPlanRequest request) {
        Map<String, String> errors = CreateBusinessPlanValidation.validate(request);
        if (errors.isEmpty()) {
            //success
            BusinessPlan businessPlan = businessPlanRepo.save(BusinessPlan.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .duration(request.getDuration())
                    .build()
            );
            importPlanService(request, businessPlan);
            return CreateBusinessPlanResponse.builder()
                    .status("200")
                    .message("Created business plan successfully")
                    .build();
        }
        return errors;
    }

    private void importPlanService(CreateBusinessPlanRequest request, BusinessPlan businessPlan) {
        List<PlanService> planServices = new ArrayList<>();
        for (CreateBusinessPlanRequest.BusinessPlanService service : request.getServiceList()) {
            BusinessService businessService = businessServiceRepo.findById(service.getId()).orElse(null);
            assert businessService != null;
            PlanService planService = PlanService.builder()
                    .businessPlan(businessPlan)
                    .businessService(businessService)
                    .build();
            planServices.add(planService);
        }
        planServiceRepo.saveAll(planServices);
    }

    //-------------------------------------UPDATE BUSINESS PLAN------------------------------------------//

    @Override
    public String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model) {
        Object output = updateBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessPlanResponse.class)){
            model.addAttribute("msg", (UpdateBusinessPlanResponse)output);
            return "home";
        }
        model.addAttribute("error", (Map<String, String>)output);
        return "home";

    }

    @Override
    public UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request) {
        Object output = updateBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateBusinessPlanResponse.class)){
            return (UpdateBusinessPlanResponse) output;
        }
        return UpdateBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    public Object updateBusinessPlanLogic(UpdateBusinessPlanRequest request) {
        Map<String, String> errors = UpdateBusinessPlanValidation.validate(request);
        if (errors.isEmpty()) {
            BusinessPlan businessPlan = businessPlanRepo.findById(request.getId()).orElse(null);
            assert businessPlan != null;
            businessPlan.setName(request.getName());
            businessPlan.setPrice(request.getPrice());
            businessPlan.setDescription(request.getDescription());
            businessPlan.setDuration(request.getDuration());
            return UpdateBusinessPlanResponse.builder()
                    .status("200")
                    .message("Updated business plan successfully")
                    .build();
        }
        return errors;
    }

}
