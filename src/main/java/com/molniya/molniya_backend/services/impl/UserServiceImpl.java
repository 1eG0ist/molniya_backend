package com.molniya.molniya_backend.services.impl;

import com.molniya.molniya_backend.dtos.user.UpdateUserRequestDto;
import com.molniya.molniya_backend.dtos.user.UserResponseDto;
import com.molniya.molniya_backend.enums.FileGroup;
import com.molniya.molniya_backend.exceptions.access.NoAccessToOperationException;
import com.molniya.molniya_backend.exceptions.no_data.NotFoundDataException;
import com.molniya.molniya_backend.mappers.UserMapper;
import com.molniya.molniya_backend.models.User;
import com.molniya.molniya_backend.repositories.UserRepository;
import com.molniya.molniya_backend.services.UserService;
import com.molniya.molniya_backend.utils.services.FilesService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FilesService filesService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto updateUser(UpdateUserRequestDto updatedDto, MultipartFile photo) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!userId.equals(updatedDto.getId())) throw new NoAccessToOperationException("Вы не можете редактировать чужой профиль");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException("Пользователь не найден"));

        // обновляем простые поля
        if (updatedDto.getUsername() != null) user.setUsername(updatedDto.getUsername());
        if (updatedDto.getFullName() != null) user.setFullName(updatedDto.getFullName());
        if (updatedDto.getEmail() != null) user.setEmail(updatedDto.getEmail());
        if (updatedDto.getBirthday() != null) user.setBirthday(updatedDto.getBirthday());

        // обработка фото
        if (updatedDto.isPhotoDeleted()) {
            if (user.getPhotoUrl() != null) {
                filesService.deleteFile(user.getPhotoUrl());
                user.setPhotoUrl(null);
            }
        } else if (photo != null && !photo.isEmpty()) {
            // если было старое фото — удаляем
            if (user.getPhotoUrl() != null) {
                filesService.deleteFile(user.getPhotoUrl());
            }

            // сохраняем новое
            String savedPath = filesService.saveFile(photo, FileGroup.USERPHOTO);
            user.setPhotoUrl(savedPath);
        }

        user.setUpdatedAt(OffsetDateTime.now());

        User updatedUser = userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toDto(updatedUser);
        System.out.println(updatedUser);
        System.out.println(userResponseDto);

        return userResponseDto;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException("Пользователь не найден"));
    }
}
