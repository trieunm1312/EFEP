package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.CreateBusinessPlanRequest;
import com.team1.efep.models.request_models.DisableBusinessPlanRequest;
import com.team1.efep.models.request_models.UpdateBusinessPlanRequest;
import com.team1.efep.models.response_models.CreateBusinessPlanResponse;
import com.team1.efep.models.response_models.DisableBusinessPlanResponse;
import com.team1.efep.models.response_models.UpdateBusinessPlanResponse;
import com.team1.efep.repositories.*;
import com.team1.efep.services.AdminService;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.ChangePasswordValidation;
import com.team1.efep.validations.CreateBusinessPlanValidation;
import com.team1.efep.validations.DisableBusinessPlanValidation;
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
    private final SellerRepo sellerRepo;

    //-------------------------------------CREATE BUSINESS PLAN(FE CHUA LAM)------------------------------------------//
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

    //-------------------------------------UPDATE BUSINESS PLAN(FE CHUA LAM)------------------------------------------//

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

    //-------------------------------------DISABLE BUSINESS PLAN(FE CHUA LAM)------------------------------------------//

    @Override
    public String disableBusinessPlan(DisableBusinessPlanRequest request, Model model) {
        Object output = disableBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, DisableBusinessPlanResponse.class)){
            model.addAttribute("msg", (DisableBusinessPlanResponse)output);
            return "home";
        }
        model.addAttribute("error", (Map<String, String>)output);
        return "home";
    }

    @Override
    public DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request) {
        Object output = disableBusinessPlanLogic(request);
        if(OutputCheckerUtil.checkIfThisIsAResponseObject(output, DisableBusinessPlanResponse.class)){
            return (DisableBusinessPlanResponse) output;
        }
        return DisableBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>)output))
                .build();
    }

    public Object disableBusinessPlanLogic(DisableBusinessPlanRequest request) {
        Map<String, String> errors = DisableBusinessPlanValidation.validate(request);
        if(errors.isEmpty()) {
            BusinessPlan businessPlan = businessPlanRepo.findById(request.getId()).orElse(null);
            assert businessPlan != null;
            businessPlan.setStatus(Status.BUSINESS_PLAN_STATUS_DISABLED);
            businessPlanRepo.save(businessPlan);
            return DisableBusinessPlanResponse.builder()
                    .status("200")
                    .message("Disabled successfully")
                    .build();
        }
        return errors;
    }

}
