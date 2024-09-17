package com.team1.efep.models.request_models;

import com.team1.efep.models.entity_models.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewFlowerListForSellerRequest {
    private int sellerId;
}
