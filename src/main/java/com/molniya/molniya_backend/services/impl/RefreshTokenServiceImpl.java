package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.models.RefreshToken;
import com.molniya.molniya_backend.models.User;
import com.molniya.molniya_backend.repositories.RefreshTokenRepository;
import com.molniya.molniya_backend.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh_lifetime}")
    private String refreshTokenLifetime;

    @Override
    @Transactional
    public RefreshToken createToken(User user) {
        // Отзываем все существующие токены пользователя
        refreshTokenRepository.revokeAllByUserId(user.getId());

        Instant expiresAt = Instant.now().plus(parseLifetime(refreshTokenLifetime), ChronoUnit.MILLIS);

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

    private long parseLifetime(String lifetime) {
        long time;
        char unit = lifetime.charAt(lifetime.length() - 1);
        time = Long.parseLong(lifetime.substring(0, lifetime.length() - 1));

        return switch (unit) {
            case 'm' -> time * 60 * 1000; // минуты
            case 'h' -> time * 60 * 60 * 1000; // часы
            case 'd' -> time * 24 * 60 * 60 * 1000; // дни
            default -> throw new IllegalArgumentException("Неизвестная единица измерения времени: " + unit);
        };
    }
}
