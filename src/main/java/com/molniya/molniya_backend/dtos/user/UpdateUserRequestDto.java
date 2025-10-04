package com.molniya.molniya_backend.dtos.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UpdateUserRequestDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private LocalDate birthday;
    private MultipartFile photo;
}
