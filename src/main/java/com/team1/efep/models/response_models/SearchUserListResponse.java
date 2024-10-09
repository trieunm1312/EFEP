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
public class SearchUserListResponse {

    private String status;

    private String message;

    private List<User> userList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class User {

        private int id;

        private String name;

        private LocalDate createdDate;
    }
}
