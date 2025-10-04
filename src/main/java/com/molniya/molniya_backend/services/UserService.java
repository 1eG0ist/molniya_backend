package com.molniya.molniya_backend.services;

import com.molniya.molniya_backend.dtos.user.UpdateUserRequestDto;
import com.molniya.molniya_backend.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User updateUser(UpdateUserRequestDto updateRequest);
    User findById(Long userId);
    String updateUserPhoto(Long userId, MultipartFile photoFile);
}
