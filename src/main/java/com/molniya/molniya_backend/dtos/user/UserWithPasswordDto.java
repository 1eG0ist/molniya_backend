package com.molniya.molniya_backend.dtos.user;

import com.molniya.molniya_backend.models.User;

/**
 * Проекция для получения пользователя вместе с его паролем.
 */
public interface UserWithPasswordDto {
    User getUser();
    String getPassword();
}

