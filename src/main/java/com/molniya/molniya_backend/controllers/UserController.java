package com.molniya.molniya_backend.controllers;

import com.molniya.molniya_backend.dtos.user.UpdateUserRequestDto;
import com.molniya.molniya_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    final UserService userService;

    @PatchMapping
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequestDto user) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
