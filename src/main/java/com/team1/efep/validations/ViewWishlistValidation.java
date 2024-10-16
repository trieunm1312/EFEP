package com.team1.efep.validations;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.repositories.AccountRepo;
import java.util.HashMap;
import java.util.Map;

public class ViewWishlistValidation {


    public static Map<String, String> validate(int accountId, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();

        if (accountId <= 0) {
            errors.put("accountId", "Invalid account ID");
            return errors;
        }

        Account account = accountRepo.findById(accountId).orElse(null);
        if (account == null) {
            errors.put("account", "Account does not exist");
            return errors;
        }

        if (account.getUser().getWishlist() == null || account.getUser().getWishlist().getWishlistItemList().isEmpty()) {
            errors.put("wishlist", "Wishlist is empty");
        }
        return errors;
    }
}
