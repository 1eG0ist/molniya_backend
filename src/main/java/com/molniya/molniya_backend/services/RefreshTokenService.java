package com.molniya.molniya_backend.services;

import com.molniya.molniya_backend.models.RefreshToken;
import com.molniya.molniya_backend.models.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createToken(User user);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> verifyExpiration(RefreshToken token);
    void revokeAllUserTokens(User user);
}
