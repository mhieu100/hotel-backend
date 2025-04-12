package com.application.vaccine_system.model.response;


import com.application.vaccine_system.model.User.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResUser {
    Long id;
    String fullname;
    String email;
    String phone;
    String address;
    UserRole role;
}
