package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Seller;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class CreateFlowerValidation {

    public static Map<String, String> validateInput(CreateFlowerRequest request, FlowerRepo flowerRepo, Seller seller) {
        Map<String, String> errors = new HashMap<>();

        int maxFlowers = 10;

        if (seller.getBusinessPlan() != null) {

            boolean has20FlowerService = seller.getBusinessPlan().getPlanServiceList().stream()
                    .anyMatch(planService -> planService.getBusinessService().getName().equals("+ 10 flowers"));

            boolean has30FlowerService = seller.getBusinessPlan().getPlanServiceList().stream()
                    .anyMatch(planService -> planService.getBusinessService().getName().equals("+ 20 flowers"));

            if (has20FlowerService && has30FlowerService) {
                maxFlowers = 40;
            } else if (has20FlowerService) {
                maxFlowers = 20;
            } else if(has30FlowerService) {
                maxFlowers = 30;
            }
        }

        if (seller.getFlowerList().size() >= maxFlowers) {
            return MapConfig.buildMapKey(errors, "You have reached the maximum flower limit ( " + maxFlowers + " flowers ). Upgrade your plan to add more.");
        }

        if (request.getName().trim().isEmpty()) {
            return MapConfig.buildMapKey(errors, "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            return MapConfig.buildMapKey(errors, "Flower name must be between 3 and 30 characters");
        } else if (flowerRepo.findByName(request.getName()).isPresent()) {
            return MapConfig.buildMapKey(errors, "Flower name already exists");
        } else if (!request.getName().matches("^[a-zA-Z0-9 ]*$")) {
            return MapConfig.buildMapKey(errors, "Flower name must not contain special characters");
        }

        if (request.getPrice() == null) {
            return MapConfig.buildMapKey(errors, "Price is required");
        } else if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(errors, "Price must be greater than 0");
        }

        if (request.getFlowerAmount() == null) {
            return MapConfig.buildMapKey(errors, "Price is required");
        } else if (request.getFlowerAmount() <= 0) {
            return MapConfig.buildMapKey(errors, "Price amount must be greater than 0");
        }

        if (request.getQuantity() < 0) {
            return MapConfig.buildMapKey(errors, "Quantity must be greater than 0");
        }

        return errors;
    }


}
