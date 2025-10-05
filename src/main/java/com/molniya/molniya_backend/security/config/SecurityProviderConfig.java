package com.molniya.molniya_backend.security.config;

import com.molniya.molniya_backend.security.security_services.Impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация провайдера аутентификации.
 * ---
 * Этот класс отвечает за настройку механизма аутентификации пользователей в системе.
 * AuthenticationProvider - это компонент Spring Security, который определяет, как именно
 * будет происходить проверка учетных данных пользователя.
 * ---
 * DaoAuthenticationProvider использует UserDetailsService для загрузки информации о пользователе
 * и PasswordEncoder для проверки паролей. Это стандартный механизм проверки
 * имени пользователя и пароля в Spring Security.
 * ---
 * Этот класс необходим для:
 * 1. Связывания сервиса пользователей с системой аутентификации
 * 2. Настройки механизма проверки паролей
 * 3. Централизованной конфигурации аутентификации для всего приложения
 */
@Configuration
@RequiredArgsConstructor
public class SecurityProviderConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Создает и настраивает провайдер аутентификации на основе DAO.
     *
     * @return Настроенный провайдер аутентификации
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Указываем сервис, который будет загружать данные пользователя
        provider.setUserDetailsService(userDetailsService);
        // Указываем энкодер для проверки паролей
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
