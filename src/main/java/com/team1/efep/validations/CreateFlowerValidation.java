package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class CreateFlowerValidation {

    public static Map<String, String> validateInput(CreateFlowerRequest request, FlowerRepo flowerRepo) {
        Map<String, String> errors = new HashMap<>();
//        if (request.getName().trim().isEmpty()) {
//            return MapConfig.buildMapKey(errors, "Flower name is required");
//        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
//            return MapConfig.buildMapKey(errors, "Flower name must be between 3 and 30 characters");
//        } else if (flowerRepo.findByName(request.getName()).isPresent()) {
//            return MapConfig.buildMapKey(errors, "Flower name already exists");
//        }
//
//        if (request.getPrice() == null) {
//            return MapConfig.buildMapKey(errors, "Price is required");
//        } else if (request.getPrice() <= 0) {
//            return MapConfig.buildMapKey(errors, "Price must be greater than 0");
//        }
//
//        if (request.getFlowerAmount() == null) {
//            return MapConfig.buildMapKey(errors, "Price is required");
//        } else if (request.getFlowerAmount() <= 0) {
//            return MapConfig.buildMapKey(errors, "Price amount must be greater than 0");
//        }
//
//        if (request.getQuantity() < 0) {
//            return MapConfig.buildMapKey(errors, "Quantity must be greater than 0");
//        }

        return errors;
    }


}
