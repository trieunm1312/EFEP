package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterOrderResponse {

    //-----------------------------------//
    private String status;

    private String message;

    private List<OrderBill> orderList;
    //-----------------------------------//

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderBill {

        private int orderId;

        private String buyerName;

        private LocalDateTime createDate;

        private float totalPrice;

        private String status;

        private String paymentType;

        private String paymentMethod;

        private List<Item> orderDetailList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Item {

        private String name;

        private int quantity;

        private float price;
    }
}