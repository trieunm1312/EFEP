package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.CancelBusinessPlanRequest;
import com.team1.efep.repositories.BusinessPlanRepo;

import java.util.HashMap;
import java.util.Map;

public class CancelBusinessPlanValidation {
    public static Map<String, String> validate(CancelBusinessPlanRequest request, BusinessPlanRepo businessPlanRepo) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        if (businessPlanRepo.existsById(request.getId())) {
            return MapConfig.buildMapKey(error, "Plan exists");
        }
        return error;
    }
}
