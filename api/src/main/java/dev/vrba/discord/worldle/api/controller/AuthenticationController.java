package dev.vrba.discord.worldle.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/auth")
@CrossOrigin(value = "*", allowCredentials = "false", allowedHeaders = "*")
public class AuthenticationController {

    @PostMapping("/check")
    public ResponseEntity<?> getChallengeForDate() {
        return ResponseEntity.ok().build();
    }
}
