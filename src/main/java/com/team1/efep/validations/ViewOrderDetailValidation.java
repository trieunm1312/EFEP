package com.team1.efep.validations;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.request_models.ViewOrderDetailRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewOrderDetailValidation {
    public static Map<String, String> validate(ViewOrderDetailRequest request, Account account, Order order) {
        Map<String, String> errors = new HashMap<>();
        if (request.getOrderId() <= 0) {
            errors.put("orderId_error", "Invalid order ID.");
        }

        if (account == null) {
            errors.put("account_error", "Account does not exist.");
        } else if (!Role.checkIfThisAccountIsBuyer(account)) {
            errors.put("role_error", "Account must be a buyer to view order details.");
        }

        if (order == null) {
            errors.put("order_error", "Order does not exist.");
        }

        return errors;
    }

}
