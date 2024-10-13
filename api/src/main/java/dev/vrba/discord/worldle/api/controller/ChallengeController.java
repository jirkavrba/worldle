package dev.vrba.discord.worldle.api.controller;

import dev.vrba.discord.worldle.api.dto.ChallengeDto;
import dev.vrba.discord.worldle.api.mapper.ChallengeMapper;
import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bot/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    @NonNull
    private final ChallengeService service;

    @NonNull
    private final ChallengeMapper mapper;

    @GetMapping("/today")
    public ResponseEntity<?> getChallengeForToday() {
        final Challenge challenge = service.findOrCreateChallengeForToday();
        final ChallengeDto response = mapper.challengeToChallengeDto(challenge);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getChallengeForDate(@NonNull @PathVariable("date") LocalDate date) {
        final Challenge challenge = service.findOrCreateChallengeByDate(date);
        final ChallengeDto response = mapper.challengeToChallengeDto(challenge);

        return ResponseEntity.ok(response);
    }

}
