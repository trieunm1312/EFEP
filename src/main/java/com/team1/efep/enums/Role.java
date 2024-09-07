package com.team1.efep.enums;

import com.team1.efep.models.entity_models.Account;

public class Role {
    public static final String BUYER = "buyer";
    public static final String SELLER = "seller";
    public static final String ADMIN = "admin";

    public static boolean checkIfThisAccountIsBuyer(Account account) {
        return account.getRole().equals(BUYER);
    }

    public static boolean checkIfThisAccountIsSeller(Account account) {
        return account.getRole().equals(SELLER);
    }

    public static boolean checkIfThisAccountIsAdmin(Account account) {
        return account.getRole().equals(ADMIN);
    }
}
