package com.molniya.molniya_backend.security.security_services;

import com.molniya.molniya_backend.security.config.CustomUserDetails;

import java.util.List;

public interface AccessTokenService {
    String generateToken(CustomUserDetails userDetails);
//    String getUserPhone(String token);
    String getUserId(String token);
    List<String> getRoles(String token);
}
