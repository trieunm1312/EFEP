package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeedbackResponse {

    private String status;
    private String message;
    private FeedbackInfo feedback;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackInfo {
        private Integer id;
        private String buyerName;
        private String content;
        private int rating;
        private LocalDateTime createDate;
    }

}
