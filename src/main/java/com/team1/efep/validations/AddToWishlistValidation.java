package com.team1.efep.validations;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Flower;
import com.team1.efep.models.request_models.AddToWishlistRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.FlowerRepo;

import java.util.HashMap;
import java.util.Map;

public class AddToWishlistValidation {

    public static Map<String, String> validate(AddToWishlistRequest request, AccountRepo accountRepo, FlowerRepo flowerRepo) {
        Map<String, String> errors = new HashMap<>();
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        if (account == null) {
            errors.put("account", "Account does not exist");
            return errors;
        }

        if (!Role.checkIfThisAccountIsBuyer(account)) {
            errors.put("role", "Only buyers can add items to wishlist");
            return errors;
        }

        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        if (flower == null) {
            errors.put("flower", "Flower does not exist");
            return errors;
        }

        if (!"AVAILABLE".equals(flower.getStatus())) {
            errors.put("flowerStatus", "Flower is not available for purchase");
            return errors;
        }

        if (flower.getQuantity() <= 0) {
            errors.put("flowerQuantity", "Flower is out of stock");
        }
        return errors;
    }
}
