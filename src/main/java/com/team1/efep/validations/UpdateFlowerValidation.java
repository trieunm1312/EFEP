package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.request_models.UpdateFlowerRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateFlowerValidation {

    public static Map<String, String> validate(UpdateFlowerRequest request, FlowerRepo flowerRepo, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        if (request.getFlowerId() <= 0) {
            return MapConfig.buildMapKey(error, "Invalid flower ID");
        } else if (flowerRepo.findById(request.getFlowerId()).isEmpty()) {
            return MapConfig.buildMapKey(error, "Flower not found");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return MapConfig.buildMapKey(error, "Flower name is required");
        } else if (request.getName().length() < 3 || request.getName().length() > 30) {
            return MapConfig.buildMapKey(error, "Flower name must be between 3 and 30 characters");
        } else if (!request.getName().matches("^[a-zA-Z0-9 ]*$")) {
            return MapConfig.buildMapKey(error, "Flower name must not contain special characters");
        } else if (flowerRepo.findAllBySeller_IdAndName(account.getUser().getSeller().getId(), request.getName()).size() > 1) {
            return MapConfig.buildMapKey(error, "Flower name already exists");
        }

        if (request.getPrice() <= 0) {
            return MapConfig.buildMapKey(error, "Price must be greater than 0");
        }

        if (request.getFlowerAmount() <= 0) {
            return MapConfig.buildMapKey(error, "Flower amount cannot be less than 1");
        }

        if (request.getQuantity() < 0) {
            return MapConfig.buildMapKey(error, "Quantity must be greater than or equal to 0");
        }

        return error;
    }
}
