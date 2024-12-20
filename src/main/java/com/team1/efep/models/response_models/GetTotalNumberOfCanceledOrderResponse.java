package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTotalNumberOfCanceledOrderResponse {

    private String status;

    private String message;

    private long getTotalNumberOfCanceledOrder;
}
