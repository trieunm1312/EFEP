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

    private String keyword;

    private List<User> userList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class User{

        private int id;

        private String name;

        private String phone;

        private String avatar;

        private LocalDate createdDate;

        private ViewUserListResponse.Account accountUser;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Account{

        private int id;

        private String email;

        private String status;

        private String role;
    }
}
