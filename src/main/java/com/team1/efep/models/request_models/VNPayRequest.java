package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPayRequest {

    private float amount;

    private int flowerId;

    private int quantity;

    private String destination;

    private String phone;
}
