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

        private String background;

        private Account accountUser;
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
