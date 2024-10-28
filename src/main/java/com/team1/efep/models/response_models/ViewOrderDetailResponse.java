package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewOrderDetailResponse {

    private String status;

    private String message;

    private int orderId;

    private String sellerName;

    private float totalPrice;

    private String orderStatus;

    private String paymentMethod;

    private List<Detail> detailList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {

        private String image;

        private String description;

        private List<String> categories;

        private String flowerName;

        private int quantity;

        private float price;
    }
}
