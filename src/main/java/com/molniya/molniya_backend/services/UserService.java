package com.molniya.molniya_backend.services;

import com.molniya.molniya_backend.dtos.user.UpdateUserRequestDto;
import com.molniya.molniya_backend.dtos.user.UserResponseDto;
import com.molniya.molniya_backend.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponseDto updateUser(UpdateUserRequestDto updatedDto, MultipartFile photo);
    User findById(Long userId);
}
