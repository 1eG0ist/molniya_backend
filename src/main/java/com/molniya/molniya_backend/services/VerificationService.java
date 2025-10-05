package com.molniya.molniya_backend.services;

import com.molniya.molniya_backend.dtos.auth.PhoneVerificationResponse;

public interface VerificationService {
    PhoneVerificationResponse sendVerificationCode(String phone);
    void verifyCode(String phone, String code, String verificationToken);
}
