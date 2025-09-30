package com.molniya.molniya_backend.services;

import com.molniya.molniya_backend.dtos.auth.JwtResponseDto;
import com.molniya.molniya_backend.dtos.auth.PhoneVerificationResponse;

public interface UserAuthService {
    PhoneVerificationResponse sendVerificationCode(String phone);
    JwtResponseDto verifyCodeAndAuthenticate(String phone, String code, String verificationToken);
    JwtResponseDto refreshToken(String refreshToken);
}
