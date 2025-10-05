package com.molniya.molniya_backend.repositories;

import com.molniya.molniya_backend.models.RefreshToken;
import com.molniya.molniya_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    @Query("SELECT r FROM RefreshToken r WHERE r.token = :token AND r.expiryDate > :now AND r.revoked = false")
    Optional<RefreshToken> findValidByToken(String token, Instant now);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user.id = :userId")
    void revokeAllByUserId(Long userId);
}
