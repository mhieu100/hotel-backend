package com.application.vaccine_system.model.response;


import com.application.vaccine_system.model.User.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResLogin {
    private String access_token;
    private UserLogin user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserLogin {
        private long id;
        private String email;
        private String fullname;
        private String phone;
        private String address;
        private UserRole roleName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        public UserLogin user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String email;
        private String fullName;
    }
}