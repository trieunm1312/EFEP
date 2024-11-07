package com.team1.efep.models.request_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeedbackRequest {
    private Integer accountId;
    private Integer sellerId;
    private Integer orderId;
    private String content;
    private int rating;
}
