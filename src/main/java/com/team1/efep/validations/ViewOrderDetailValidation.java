package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.request_models.ViewOrderDetailRequest;

import java.util.HashMap;
import java.util.Map;

public class ViewOrderDetailValidation {
    public static Map<String, String> validate(ViewOrderDetailRequest request, Account account, Order order) {
        Map<String, String> error = new HashMap<>();
        if (request.getOrderId() <= 0) {
            return MapConfig.buildMapKey(error, "Invalid order ID.");
        }

        if (account == null) {
            return MapConfig.buildMapKey(error, "Account does not exist.");
        } else if (!Role.checkIfThisAccountIsBuyer(account)) {
            return MapConfig.buildMapKey(error, "Account must be a buyer to view order details.");
        }

        if (order == null) {
            return MapConfig.buildMapKey(error, "Order does not exist.");
        }

        return error;
    }

}
