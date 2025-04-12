package com.application.vaccine_system.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqLogin {
    @NotBlank(message = "Email không được trống")
    private String username;
    @NotBlank(message = "Password không được trống")
    private String password;
}