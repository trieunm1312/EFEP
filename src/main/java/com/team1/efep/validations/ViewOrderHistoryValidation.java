package com.team1.efep.validations;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Order;
import com.team1.efep.models.entity_models.OrderDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewOrderHistoryValidation {
    public static Map<String, String> orderHistoryValidation(Account account, List<Order> orderList) {
        Map<String, String> error = new HashMap<>();
        if (account == null) {
            return MapConfig.buildMapKey(error, "Account does not exist.");
        } else if (!Role.checkIfThisAccountIsBuyer(account)) {
            return MapConfig.buildMapKey(error, "Account must be a buyer to view order history.");
        }

        if (orderList == null || orderList.isEmpty()) {
            return MapConfig.buildMapKey(error, "No orders found for this account.");
        }

        for (Order order : orderList) {
            if (order.getOrderDetailList() == null || order.getOrderDetailList().isEmpty()) {
                return MapConfig.buildMapKey(error, "Order ID " + order.getId() + " has no details.");
            }

            for (OrderDetail detail : order.getOrderDetailList()) {
                if (detail.getFlower() == null) {
                    return MapConfig.buildMapKey(error, "Order ID " + order.getId() + " has a missing flower.");
                }

                if (detail.getQuantity()     <= 0) {
                    return MapConfig.buildMapKey(error,  "Order ID " + order.getId() + " has invalid quantity.");
                }
            }
        }
        return error;
    }
}
