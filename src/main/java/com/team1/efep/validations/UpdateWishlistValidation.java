package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.WishlistItem;
import com.team1.efep.models.request_models.UpdateWishlistRequest;
import com.team1.efep.repositories.WishlistItemRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateWishlistValidation {
    public static Map<String, String> validate(UpdateWishlistRequest request, WishlistItemRepo wishlistItemRepo) {
        Map<String, String> error = new HashMap<>();
        if (request.getWishlistId() <= 0) {
            return MapConfig.buildMapKey(error, "Invalid wishlist ID.");
        }

        if (request.getWishlistItemId() == null || request.getWishlistItemId().isEmpty()) {
            return MapConfig.buildMapKey(error, "Invalid wishlist item ID.");
        }

        if (!"asc".equals(request.getRequest()) && !"desc".equals(request.getRequest())) {
            return MapConfig.buildMapKey(error, "Invalid request action. Must be 'asc' or 'desc'.");
        }

        if ("desc".equals(request.getRequest())) {
            WishlistItem wishlistItem = wishlistItemRepo.findById(Integer.parseInt(request.getWishlistItemId())).orElse(null);
            if (wishlistItem != null && wishlistItem.getQuantity() <= 1) {
                return MapConfig.buildMapKey(error, "Quantity cannot be less than 1.");
            }
        }
        return error;
    }
}
