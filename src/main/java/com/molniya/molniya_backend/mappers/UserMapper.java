package com.molniya.molniya_backend.mappers;

import com.molniya.molniya_backend.dtos.user.UserResponseDto;
import com.molniya.molniya_backend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
