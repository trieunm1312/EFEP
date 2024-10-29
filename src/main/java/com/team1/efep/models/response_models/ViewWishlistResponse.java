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
public class ViewWishlistResponse {

    String status;

    String message;

    int id;

    int userId;

    String userName;

    List<WishlistItems> wishlistItemList;

    float totalPrice;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class WishlistItems {
        int id;
        List<ViewFlowerListResponse.Image> imgList;
        String name;
        String description;
        int quantity;
        float price;
        int stockQuantity;
    }

}
