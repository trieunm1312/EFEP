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
public class ViewCartResponse {

    private String status;

    private String message;

    private int id;

    private int userId;

    private String userName;

    private List<CartItems> cartItemList;

    private float totalPrice;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItems {
        private int id;
        private String name;
        private int quantity;
        private float price;
    }

}
