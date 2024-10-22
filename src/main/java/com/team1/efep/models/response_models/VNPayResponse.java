package com.team1.efep.models.response_models;

import com.team1.efep.models.entity_models.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPayResponse {
    private String status;
    private String message;
    private String paymentURL;
    private Order order;
}
