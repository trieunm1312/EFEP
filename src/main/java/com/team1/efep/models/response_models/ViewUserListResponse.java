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
public class ViewUserListResponse {
    private String status;
    private String message;
    private List<User> users;
    private List<Account> accounts;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class User{
        private String id;
        private String name;
        private String phone;
        private String avatar;
        private String background;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Account{
        private String id;
        private String role;
        private String status;
    }
}
