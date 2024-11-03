package com.team1.efep.models.response_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewNotificationResponse {

    private String status;

    private String message;

    private List<NotificationDetail> notificationList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NotificationDetail {
        private int id;
        private String content;
    }
}
