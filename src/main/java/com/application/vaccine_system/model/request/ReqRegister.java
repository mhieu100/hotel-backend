package com.application.vaccine_system.model.request;


import com.application.vaccine_system.model.User.UserRole;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqRegister {
    @NotBlank(message = "Email không được trống")
    private String email;
    @NotBlank(message = "Full name không được trống")
    private String fullname;
    @NotBlank(message = "Password không được trống")
    private String password;
    private UserRole role;
}