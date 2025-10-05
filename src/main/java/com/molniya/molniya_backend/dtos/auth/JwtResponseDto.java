package com.molniya.molniya_backend.dtos.auth;

import com.molniya.molniya_backend.dtos.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;
    private boolean newUser;
}
