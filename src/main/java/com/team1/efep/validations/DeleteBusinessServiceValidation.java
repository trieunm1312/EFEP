package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.DeleteBusinessServiceRequest;
import com.team1.efep.repositories.BusinessServiceRepo;

import java.util.HashMap;
import java.util.Map;

public class DeleteBusinessServiceValidation {
    public static Map<String, String> validate(DeleteBusinessServiceRequest request, BusinessServiceRepo businessServiceRepo) {
        Map<String, String> error = new HashMap<>();
        // code validate here
        if(!businessServiceRepo.existsById(request.getId())) {
            return MapConfig.buildMapKey(error, "Service does not exist");
        }
        return error;
    }
}
