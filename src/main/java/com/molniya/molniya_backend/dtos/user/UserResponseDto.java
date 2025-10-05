package com.molniya.molniya_backend.dtos.user;

import com.molniya.molniya_backend.models.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;

@Data
public class UserResponseDto {
    private Long id;

    private String username;

    private String fullName;

    private String phone;

    private String email;

    private LocalDate birthday;

    private String photoUrl;

    private Float coins = 0.0f;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    private OffsetDateTime updatedAt = OffsetDateTime.now();

    private Collection<Role> roles;
}
