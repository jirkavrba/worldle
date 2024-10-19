package dev.vrba.discord.worldle.api.controller;

import dev.vrba.discord.worldle.api.dto.ChallengeDto;
import dev.vrba.discord.worldle.api.mapper.ChallengeMapper;
import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/admin/challenge")
@CrossOrigin(value = "*", allowCredentials = "false", allowedHeaders = "*")
public class ChallengeController {

    private final ChallengeService service;

    private final ChallengeMapper mapper;

    public ChallengeController(
            @NonNull ChallengeService service,
            @NonNull ChallengeMapper mapper
    ) {
        this.service = Objects.requireNonNull(service);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getChallengeForDate(@NonNull @PathVariable("date") LocalDate date) {
        final Challenge challenge = service.findOrCreateChallengeByDate(date);
        final ChallengeDto response = mapper.challengeToChallengeDto(challenge);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/date/{date}/regenerate")
    public ResponseEntity<?> regenerateChallengeForDate(@NonNull @PathVariable("date") LocalDate date) {
        // TODO: Implement this
        return ResponseEntity.accepted().build();
    }
}
