package com.team1.efep.validations;

import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.repositories.FlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class CreateFlowerValidation {

    public static Map<String, String> validateInput(CreateFlowerRequest request, FlowerRepo flowerRepo) {
        Map<String, String> errors = new HashMap<>();
        if(flowerRepo.findByName(request.getName()).isPresent()){
            errors.put("name", "Flower name is existed");
        }else if (request.getName() == null || request.getName().trim().isEmpty()) {
            errors.put("name", "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            errors.put("name", "Flower name must be between 3 and 30 characters");
        }

        if (request.getPrice() <= 0) {
            errors.put("price", "Price must be greater than 0");
        }

        if (request.getFlowerAmount() <= 0) {
            errors.put("amount", "Price amount must be greater than 0");
        }

        if (request.getQuantity() < 0) {
            errors.put("quantity", "Quantity must be greater than 0");
        }

        return errors;
    }


}
