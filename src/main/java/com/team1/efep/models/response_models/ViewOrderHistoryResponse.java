package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewOrderHistoryResponse {

    private String status;

    private String message;

    private List<Order> orderList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Order {

        private int orderId;

        private String sellerName;

        private float totalPrice;

        private String status;

        private List<Detail> detailList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {

        private String flowerName;

        private int quantity;

        private float price;
    }
}

