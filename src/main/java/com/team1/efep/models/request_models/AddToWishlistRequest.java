package com.team1.efep.models.request_models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToWishlistRequest {

    Integer accountId;

    Integer flowerId;

    int quantity;

//    This is for case clicking add to wishlist when currently stand in category page
    String keyword;

}
