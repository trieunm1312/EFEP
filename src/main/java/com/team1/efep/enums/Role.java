package com.team1.efep.enums;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.repositories.AccountRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
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

    public static Account getCurrentLoggedAccount(HttpSession session) {
        return session.getAttribute("acc") != null ? (Account) session.getAttribute("acc") : null;
    }

    public static Account getCurrentLoggedAccount(int id, AccountRepo accountRepo) {
        return accountRepo.findById(id).orElse(null);
    }
}
