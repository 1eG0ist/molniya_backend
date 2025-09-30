package com.molniya.molniya_backend.dtos.auth;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String phone;
    private String code;
    private String verificationToken;
}
