package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.BusinessService;
import com.team1.efep.models.request_models.UpdateBusinessPlanRequest;
import com.team1.efep.repositories.BusinessPlanRepo;
import com.team1.efep.repositories.BusinessServiceRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateBusinessPlanValidation {
    private static final int MAX_NAME_LENGTH = 20; // Define maximum length for name
    private static final int MAX_DESCRIPTION_LENGTH = 500; // Define maximum length for description

    public static Map<String, String> validate(UpdateBusinessPlanRequest request, BusinessPlanRepo businessPlanRepo, BusinessServiceRepo businessServiceRepo) {
        Map<String, String> error = new HashMap<>();
        // Check if the ID is provided and exists in the repository
        if (!businessPlanRepo.existsById(request.getId())) {
            return MapConfig.buildMapKey(error, "Business plan does not exist.");
        }

        // Validate the name
        if (request.getName().isEmpty()) {
            return MapConfig.buildMapKey(error, "Name is required.");
        }

        if (request.getName().length() > MAX_NAME_LENGTH) {
            return MapConfig.buildMapKey(error, "Name cannot exceed " + MAX_NAME_LENGTH + " characters.");
        }

        // Validate the price
        if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Price must be provided and should not be negative.");
        }

        // Validate the description
        if (request.getDescription().isEmpty()) {
            return MapConfig.buildMapKey(error, "Description is required.");
        }

        if (request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return MapConfig.buildMapKey(error, "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        // Validate the duration
        if (request.getDuration() <= 0) {
            return MapConfig.buildMapKey(error, "Duration must be provided and greater than 0.");
        }

        // Validate the business service list
        if (request.getBusinessServiceList().isEmpty()) {
            return MapConfig.buildMapKey(error, "At least one business service must be selected.");
        }

        List<Integer> validServiceIdList = businessServiceRepo.findAll().stream()
                .filter(service -> request.getBusinessServiceList().contains(service.getId()))
                .map(BusinessService::getId)
                .toList();

        if (validServiceIdList.isEmpty()) {
            return MapConfig.buildMapKey(error, "Business service does not exist.");
        }

        return error; // Return the final map of errors, empty means validation passed
    }
}
