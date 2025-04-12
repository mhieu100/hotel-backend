package com.application.vaccine_system.model.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqUser {
    @NotBlank(message = "Full name không được trống")
    String fullname;
    String email;
    String phone;
    String address;
    String roleName;
}