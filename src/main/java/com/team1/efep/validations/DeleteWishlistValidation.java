package com.team1.efep.validations;

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
            errors.put("wishlistId_error", "Invalid wishlist ID.");
        }

        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            errors.put("account_error", "Invalid account or unauthorized access.");
        } else {
            Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
            if (wishlist == null || !wishlist.getUser().getId().equals(account.getUser().getId())) {
                errors.put("ownership_error", "Wishlist does not belong to the current user.");
            }
        }
        return errors;
    }
}
