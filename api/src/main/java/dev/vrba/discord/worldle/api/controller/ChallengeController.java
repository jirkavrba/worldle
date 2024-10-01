package dev.vrba.discord.worldle.api.controller;

import dev.vrba.discord.worldle.api.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    @NonNull
    private final ChallengeService service;

    @Secured("ROLE_BOT")
    @GetMapping("/today")
    public ResponseEntity<?> getChallengeForToday() {
        return ResponseEntity.ok(service.findOrCreateChallengeForToday());
    }

}
