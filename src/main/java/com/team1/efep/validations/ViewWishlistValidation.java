package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.repositories.AccountRepo;
import java.util.HashMap;
import java.util.Map;

public class ViewWishlistValidation {


    public static Map<String, String> validate(int accountId, AccountRepo accountRepo) {
        Map<String, String> error = new HashMap<>();

        if (accountId <= 0) {
            return MapConfig.buildMapKey(error, "Invalid account id");
        }

        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        if (account == null) {
            return MapConfig.buildMapKey(error, "You are not logged in");
        }

        if (account.getUser().getWishlist() == null || account.getUser().getWishlist().getWishlistItemList().isEmpty()) {
            return MapConfig.buildMapKey(error, "Wishlist is empty");
        }
        return error;
    }
}
