package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.models.request_models.DeleteWishlistRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.WishlistRepo;

import java.util.HashMap;
import java.util.Map;

public class DeleteWishlistValidation {
    public static Map<String, String> validate(DeleteWishlistRequest request, AccountRepo accountRepo, WishlistRepo wishlistRepo) {
        Map<String, String> errors = new HashMap<>();
        if ( request.getWishlistId() <= 0) {
            return MapConfig.buildMapKey(errors, "Invalid wishlist ID.");
        }

        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return MapConfig.buildMapKey(errors, "Invalid account or unauthorized access.");
        } else {
            Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
            if (wishlist == null || !wishlist.getUser().getId().equals(account.getUser().getId())) {
                return MapConfig.buildMapKey(errors, "Wishlist does not belong to the current user.");
            }
        }
        return errors;
    }
}
