package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.WishlistItem;
import com.team1.efep.models.request_models.UpdateWishlistRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.WishlistItemRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateWishlistValidation {
    public static Map<String, String> validate(UpdateWishlistRequest request, WishlistItemRepo wishlistItemRepo, AccountRepo  accountRepo) {
        Map<String, String> errors = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return MapConfig.buildMapKey(errors, "You are not logged in");
        }

        if (request.getWishlistId() <= 0) {
            return MapConfig.buildMapKey(errors, "Invalid wishlist ID.");
        }

        if (request.getWishlistItemId() < 0) {
            return MapConfig.buildMapKey(errors, "Invalid wishlist item ID.");
        }

        if (!"asc".equals(request.getRequest()) && !"desc".equals(request.getRequest())) {
            return MapConfig.buildMapKey(errors, "Invalid request action. Must be 'asc' or 'desc'.");
        }

        if ("desc".equals(request.getRequest())) {
            WishlistItem wishlistItem = wishlistItemRepo.findById(request.getWishlistItemId()).orElse(null);
            if (wishlistItem != null && wishlistItem.getQuantity() <= 1) {
                return MapConfig.buildMapKey(errors, "Quantity cannot be less than 1.");
            }
        }
        return errors;
    }
}
