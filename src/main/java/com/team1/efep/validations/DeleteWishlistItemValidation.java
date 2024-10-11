package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.models.entity_models.WishlistItem;
import com.team1.efep.models.request_models.DeleteWishlistItemRequest;
import com.team1.efep.repositories.AccountRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DeleteWishlistItemValidation {
    public static Map<String, String> validate(DeleteWishlistItemRequest request, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        if (account == null) {
            return MapConfig.buildMapKey(errors, "Account does not exist");
        }

        Wishlist wishlist = account.getUser().getWishlist();
        Optional<WishlistItem> wishlistItem = wishlist.getWishlistItemList().stream()
                .filter(item -> Objects.equals(item.getId(), request.getWishlistItemId()))
                .findFirst();

        if (wishlistItem.isEmpty()) {
            return MapConfig.buildMapKey(errors, "Wishlist item does not exist or does not belong to the account");
        }
        return errors;
    }
}
