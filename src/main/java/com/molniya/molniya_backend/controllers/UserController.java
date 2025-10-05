package com.molniya.molniya_backend.controllers;

import com.molniya.molniya_backend.dtos.user.UpdateUserRequestDto;
import com.molniya.molniya_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    final UserService userService;

    @PatchMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUser(
            @RequestPart("updatedDto") UpdateUserRequestDto updatedDto,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            return ResponseEntity.ok(userService.updateUser(updatedDto, photo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
