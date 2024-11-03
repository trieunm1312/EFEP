package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewProfileResponse {

    private String status;

    private String message;

    private int id;

    private String name;

    private String email;

    private String phone;

    private String avatar;

    private String background;

    private int totalFlower;

    private float sellerRating;

    private List<FeedbackDetail> feedbackList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FeedbackDetail {

        private int id;

        private String name;

        private String avatar;

        private String content;

        private int rating;

        private LocalDate createDate;
    }
}
