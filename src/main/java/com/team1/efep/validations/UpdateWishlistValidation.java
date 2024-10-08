package com.team1.efep.validations;

import com.team1.efep.models.entity_models.WishlistItem;
import com.team1.efep.models.request_models.UpdateWishlistRequest;
import com.team1.efep.repositories.WishlistItemRepo;
import com.team1.efep.repositories.WishlistRepo;

import java.util.HashMap;
import java.util.Map;

public class UpdateWishlistValidation {
    public static Map<String, String> validate(UpdateWishlistRequest request, WishlistItemRepo wishlistItemRepo) {
        Map<String, String> errors = new HashMap<>();
        if (request.getWishlistId() <= 0) {
            errors.put("wishlistId_error", "Invalid wishlist ID.");
        }

        if (request.getWishlistItemId() == null || request.getWishlistItemId().isEmpty()) {
            errors.put("wishlistItemId_error", "Invalid wishlist item ID.");
        }

        if (!"asc".equals(request.getRequest()) && !"desc".equals(request.getRequest())) {
            errors.put("request_error", "Invalid request action. Must be 'asc' or 'desc'.");
        }

        if ("desc".equals(request.getRequest())) {
            WishlistItem wishlistItem = wishlistItemRepo.findById(Integer.parseInt(request.getWishlistItemId())).orElse(null);
            if (wishlistItem != null && wishlistItem.getQuantity() <= 1) {
                errors.put("quantity_error", "Quantity cannot be less than 1.");
            }
        }
        return errors;
    }
}
