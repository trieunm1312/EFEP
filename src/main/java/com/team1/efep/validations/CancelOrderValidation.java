package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.request_models.CancelOrderRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.OrderRepo;

import java.util.HashMap;
import java.util.Map;

public class CancelOrderValidation {
    public static Map<String, String> validate(CancelOrderRequest request, OrderRepo orderRepo, AccountRepo accountRepo) {
        Map<String, String> errors = new HashMap<>();
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            MapConfig.buildMapKey(errors, "Order does not exist.");
            return errors;
        }

        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        if (account == null || !order.getUser().getId().equals(account.getUser().getId())) {
            MapConfig.buildMapKey(errors, "You are not authorized to cancel this order.");
            return errors;
        }

        if (!order.getStatus().equals(Status.ORDER_STATUS_PROCESSING)) {
            MapConfig.buildMapKey(errors, "Order cannot be cancelled");
        }
        return errors;
    }
}
