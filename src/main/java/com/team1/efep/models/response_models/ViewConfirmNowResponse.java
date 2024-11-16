package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ViewConfirmNowResponse {

    String status;

    String message;

    int userId;

    String userName;

    String buyerPhone;

    String address;

    float totalPrice;

    FlowerInfo flower;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class FlowerInfo {
        int id;
        List<ViewFlowerListResponse.Image> imgList;
        String name;
        String description;
        int quantity;
        float price;
        int stockQuantity;
    }

}
