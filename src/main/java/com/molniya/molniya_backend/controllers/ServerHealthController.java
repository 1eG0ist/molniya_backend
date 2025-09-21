package com.molniya.molniya_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class ServerHealthController {

    @GetMapping
    public ResponseEntity<?> getServerHealth(){
        return ResponseEntity.ok("Molniya backend server is running correctly");
    }
}
