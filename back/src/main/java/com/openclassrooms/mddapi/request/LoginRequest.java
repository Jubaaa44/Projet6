package com.openclassrooms.mddapi.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}