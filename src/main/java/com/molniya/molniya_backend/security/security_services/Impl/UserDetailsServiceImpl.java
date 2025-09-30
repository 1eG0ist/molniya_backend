package com.molniya.molniya_backend.security.security_services.Impl;

import com.molniya.molniya_backend.dtos.user.UserWithPasswordDto;
import com.molniya.molniya_backend.repositories.UserRepository;
import com.molniya.molniya_backend.security.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для загрузки пользовательских данных в контексте безопасности.
 * Реализует интерфейс UserDetailsService Spring Security, который используется
 * для загрузки данных о пользователе по его имени (email, username, телефон и т.д.).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя для аутентификации по его email или телефону.
     *
     * @param phone Идентификатор пользователя (телефон)
     * @return UserDetails объект с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserWithPasswordDto projection = userRepository.findByPhoneWithPassword(phone)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с телефоном '" + phone + "' не найден"));

        return new CustomUserDetails(projection.getUser(), projection.getPassword());

    }
}
