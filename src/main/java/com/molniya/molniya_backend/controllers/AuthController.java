package com.molniya.molniya_backend.controllers;

import com.molniya.molniya_backend.dtos.auth.*;
import com.molniya.molniya_backend.services.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аутентификации пользователей
 */
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userAuthService;

    /**
     * Отправляет код верификации на телефон
     * 
     * @param request запрос с телефоном
     * @return ответ с токеном верификации
     */
    @PostMapping("/send-code")
    public ResponseEntity<PhoneVerificationResponse> sendVerificationCode(@RequestBody PhoneVerificationRequest request) {
        return ResponseEntity.ok(userAuthService.sendVerificationCode(request.getPhone()));
    }

    /**
     * Проверяет код верификации и выполняет вход/регистрацию
     * 
     * @param request запрос с кодом, телефоном и токеном
     * @return ответ с JWT токенами и данными пользователя
     */
    @PostMapping("/verify-code")
    public ResponseEntity<JwtResponseDto> verifyCode(@RequestBody VerifyCodeRequest request) {
        return ResponseEntity.ok(userAuthService.verifyCodeAndAuthenticate(
                request.getPhone(), request.getCode(), request.getVerificationToken()));
    }

    /**
     * Обновляет JWT токен по refresh токену
     * 
     * @param request запрос с refresh токеном
     * @return ответ с новыми JWT токенами
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(userAuthService.refreshToken(request.getRefreshToken()));
    }
}
