package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.models.RefreshToken;
import com.molniya.molniya_backend.models.User;
import com.molniya.molniya_backend.repositories.RefreshTokenRepository;
import com.molniya.molniya_backend.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh_lifetime}")
    private Duration refreshTokenLifetime;

    @Override
    @Transactional
    public RefreshToken createToken(User user) {
        // Отзываем все существующие токены пользователя
        refreshTokenRepository.revokeAllByUserId(user.getId());

        Instant expiresAt = Instant.now().plus(refreshTokenLifetime);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(expiresAt);
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.isRevoked() || token.getExpiryDate().isBefore(Instant.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            return Optional.empty();
        }
        return Optional.of(token);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllByUserId(user.getId());
    }
}
