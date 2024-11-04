package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
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
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return MapConfig.buildMapKey(error, "You are not logged in");
        }

        if (!Role.checkIfThisAccountIsBuyer(account)) {
            return MapConfig.buildMapKey(error, "Only buyers can add items to wishlist");
        }

        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        if (flower == null) {
            return MapConfig.buildMapKey(error, "Flower does not exist");
        }

        if (flower.getQuantity() <= 0) {
            return MapConfig.buildMapKey(error, "Flower is out of stock");
        }
        return error;
    }
}
