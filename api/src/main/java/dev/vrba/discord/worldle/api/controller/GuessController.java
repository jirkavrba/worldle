package dev.vrba.discord.worldle.api.controller;

import dev.vrba.discord.worldle.api.dto.GuessDto;
import dev.vrba.discord.worldle.api.dto.GuessResponseDto;
import dev.vrba.discord.worldle.api.dto.GuessSubmitRequestDto;
import dev.vrba.discord.worldle.api.mapper.GuessMapper;
import dev.vrba.discord.worldle.api.model.Guess;
import dev.vrba.discord.worldle.api.service.GuessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/bot/guess")
@RequiredArgsConstructor
public class GuessController {

    @NonNull
    private final GuessService service;

    @GetMapping("/user/{user:^[0-9]+$}/today")
    public ResponseEntity<GuessResponseDto> getUserGuessForToday(@PathVariable("user") String user) {
        final GuessResponseDto response = service.findGuessForToday(user)
                .map(GuessMapper.INSTANCE::guessToGuessDto)
                .map(guess -> new GuessResponseDto(true, guess))
                .orElse(new GuessResponseDto(false, null));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{user:^[0-9]+$}/date/{date}")
    public ResponseEntity<GuessResponseDto> getUserGuessForDate(
            @PathVariable("user") String user,
            @PathVariable("date") LocalDate date
    ) {
        final GuessResponseDto response = service.findGuessForDate(date, user)
                .map(GuessMapper.INSTANCE::guessToGuessDto)
                .map(guess -> new GuessResponseDto(true, guess))
                .orElse(new GuessResponseDto(false, null));

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/submit", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GuessDto> submitUserGuess(
            @RequestBody GuessSubmitRequestDto request
    ) {
        final Guess guess = service.recordGuessForToday(request.user(), request.count());
        final GuessDto response = GuessMapper.INSTANCE.guessToGuessDto(guess);

        return ResponseEntity.ok(response);
    }
}
