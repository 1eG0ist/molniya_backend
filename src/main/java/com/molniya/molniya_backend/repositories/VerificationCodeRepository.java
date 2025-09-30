package com.molniya.molniya_backend.repositories;

import com.molniya.molniya_backend.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT v FROM VerificationCode v WHERE v.phone = :phone AND v.verificationToken = :token " +
            "AND v.expiresAt > :now")
    Optional<VerificationCode> findActiveByPhoneAndToken(String phone, String token, OffsetDateTime now);

    Optional<VerificationCode> findFirstByPhoneOrderByCreatedAtDesc(String phone);
}
