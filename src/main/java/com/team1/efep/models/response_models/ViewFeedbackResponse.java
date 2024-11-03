package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewFeedbackResponse {

    private String status;

    private String message;

    private List<FeedbackDetail> feedbackList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FeedbackDetail {

        private Integer id;

        private String userName;

        private String content;

        private int rating;
    }

}
