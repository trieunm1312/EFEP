package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterOrderRequest {
    private int accountId;
    private String status;
    private LocalDateTime createdDate;
}
