package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.dtos.auth.JwtResponseDto;
import com.molniya.molniya_backend.dtos.auth.PhoneVerificationResponse;
import com.molniya.molniya_backend.models.RefreshToken;
import com.molniya.molniya_backend.models.Role;
import com.molniya.molniya_backend.models.User;
import com.molniya.molniya_backend.repositories.RoleRepository;
import com.molniya.molniya_backend.repositories.UserRepository;
import com.molniya.molniya_backend.security.security_services.AccessTokenService;
import com.molniya.molniya_backend.services.RefreshTokenService;
import com.molniya.molniya_backend.services.UserAuthService;
import com.molniya.molniya_backend.services.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationService verificationService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    @Override
    public PhoneVerificationResponse sendVerificationCode(String phone) {
        return verificationService.sendVerificationCode(phone);
    }

    @Override
    @Transactional
    public JwtResponseDto verifyCodeAndAuthenticate(String phone, String code, String verificationToken) {
        // Проверяем код верификации
        boolean isCodeValid = verificationService.verifyCode(phone, code, verificationToken);
        if (!isCodeValid) {
            throw new BadCredentialsException("Неверный код верификации");
        }

        // Ищем пользователя по телефону или создаем нового
        Optional<User> userOpt = userRepository.findByPhone(phone);
        boolean isNewUser = userOpt.isEmpty();

        User user;
        if (isNewUser) {
            user = createUser(phone);
        } else {
            user = userOpt.get();
        }

        // Генерируем токены
        String accessToken = accessTokenService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createToken(user);

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(user)
                .newUser(isNewUser)
                .build();
    }

    @Override
    @Transactional
    public JwtResponseDto refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .flatMap(refreshTokenService::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();

                    // Отмечаем текущий токен как отозванный
                    refreshTokenService.revokeAllUserTokens(user);

                    // Создаем новые токены
                    String accessToken = accessTokenService.generateToken(user);
                    RefreshToken newRefreshToken = refreshTokenService.createToken(user);

                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .user(user)
                            .newUser(false)
                            .build();
                })
                .orElseThrow(() -> new BadCredentialsException("Недействительный или истекший refresh токен"));
    }

    /**
     * Создает нового пользователя
     *
     * @param phone номер телефона
     * @return созданный пользователь
     */
    private User createUser(String phone) {
        // Получаем роль пользователя (должна быть создана заранее)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль пользователя не найдена"));

        // Создаем пользователя с базовыми данными
        User user = new User();
        user.setPhone(phone);
        user.setUsername("user_" + UUID.randomUUID().toString().substring(0, 8));
        user.setRoles(Collections.singletonList(userRole));
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        return userRepository.save(user);
    }
}
