package com.molniya.molniya_backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.molniya.molniya_backend.dtos.auth.PhoneVerificationResponse;
import com.molniya.molniya_backend.exceptions.access.InvalidVerificationTokenException;
import com.molniya.molniya_backend.exceptions.bad_data.InvalidVerificationCodeException;
import com.molniya.molniya_backend.exceptions.no_data.NotFoundDataException;
import com.molniya.molniya_backend.services.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration EXPIRATION = Duration.ofMinutes(2);

    @Override
    public PhoneVerificationResponse sendVerificationCode(String phone) {
        String verificationToken = UUID.randomUUID().toString();
        String code = "000000"; // Для разработки, можно генерацию рандомного кода добавить

        // Создаем объект для хранения в Redis
        Map<String, String> data = Map.of(
                "code", code,
                "verificationToken", verificationToken
        );

        try {
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(phone, json, EXPIRATION);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации кода верификации для телефона {}: {}", phone, e.getMessage());
            throw new RuntimeException("Ошибка генерации кода верификации");
        }

        // TODO ТУТ ЛОГИКУ ОТПРАВКИ НА ТЕЛЕФОН ВЫНЕСЕННУЮ В ОТДЕЛЬНЫЙ СЕРВИС
        log.info("Код верификации для телефона {}: {}", phone, code);

        return PhoneVerificationResponse.builder()
                .verificationToken(verificationToken)
                .message("Код верификации отправлен на номер " + phone)
                .build();
    }

    @Override
    public void verifyCode(String phone, String code, String verificationToken) {
        String json = redisTemplate.opsForValue().get(phone);

        if (json == null) {
            throw new NotFoundDataException("Запросов на верификацию с запрашиваемым телефоном не найдено или истекло время жизни кода");
        }

        Map<String, String> data;
        try {
            data = objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации кода верификации для телефона {}: {}", phone, e.getMessage());
            throw new RuntimeException("Ошибка проверки кода верификации");
        }

        if (!data.get("code").equals(code)) {
            throw new InvalidVerificationCodeException();
        }

        if (!data.get("verificationToken").equals(verificationToken)) {
            throw new InvalidVerificationTokenException();
        }

        // Удаляем запись после успешной проверки
        redisTemplate.delete(phone);
    }
}
