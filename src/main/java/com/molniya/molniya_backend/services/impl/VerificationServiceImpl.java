package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.dtos.auth.PhoneVerificationResponse;
import com.molniya.molniya_backend.models.VerificationCode;
import com.molniya.molniya_backend.repositories.VerificationCodeRepository;
import com.molniya.molniya_backend.services.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    @Transactional
    public PhoneVerificationResponse sendVerificationCode(String phone) {
        String verificationToken = UUID.randomUUID().toString();

        // В реальном приложении здесь должна быть генерация случайного 6-значного кода
        // Для разработки используем статический код 000000
        String code = "000000";

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiresAt = now.plus(5, ChronoUnit.MINUTES); // Код действителен 5 минут

        VerificationCode verificationCode = VerificationCode.builder()
                .phone(phone)
                .code(code)
                .verificationToken(verificationToken)
                .createdAt(now)
                .expiresAt(expiresAt)
                .used(false)
                .build();

        verificationCodeRepository.save(verificationCode);

        // TODO ТУТ ЛОГИКУ ОТПРАВКИ НА ТЕЛЕФОН
        log.info("Код верификации для телефона {}: {}", phone, code);

        return PhoneVerificationResponse.builder()
                .verificationToken(verificationToken)
                .message("Код верификации отправлен на номер " + phone)
                .build();
    }

    @Override
    @Transactional
    public boolean verifyCode(String phone, String code, String verificationToken) {
        Optional<VerificationCode> codeOpt = verificationCodeRepository.findActiveByPhoneAndToken(
                phone, verificationToken, OffsetDateTime.now());

        if (codeOpt.isPresent()) {
            VerificationCode verificationCode = codeOpt.get();

            if (verificationCode.getCode().equals(code)) {
                // Удаялем и отвечаем положительным кодом
                verificationCodeRepository.delete(verificationCode);
                return true;
            }
        }

        return false;
    }
}
