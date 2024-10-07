package com.team1.efep.validations;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.entity_models.OrderDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewOrderHistoryValidation {
    public static Map<String, String> orderHistoryValidation(Account account, List<Order> orderList) {
        Map<String, String> errors = new HashMap<>();
        if (account == null) {
            errors.put("account_error", "Account does not exist.");
        } else if (!Role.checkIfThisAccountIsBuyer(account)) {
            errors.put("role_error", "Account must be a buyer to view order history.");
        }

        if (orderList == null || orderList.isEmpty()) {
            errors.put("order_error", "No orders found for this account.");
            return errors;
        }

        for (Order order : orderList) {
            if (order.getOrderDetailList() == null || order.getOrderDetailList().isEmpty()) {
                errors.put("order_detail_error", "Order ID " + order.getId() + " has no details.");
            }

            for (OrderDetail detail : order.getOrderDetailList()) {
                if (detail.getFlower() == null) {
                    errors.put("flower_error", "Order ID " + order.getId() + " has a missing flower.");
                }
                if (detail.getQuantity() <= 0) {
                    errors.put("quantity_error", "Order ID " + order.getId() + " has invalid quantity.");
                }
                if (detail.getPrice() <= 0) {
                    errors.put("price_error", "Order ID " + order.getId() + " has invalid price.");
                }
            }
        }
        return errors;
    }
}
