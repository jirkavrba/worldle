package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.Guess;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface GuessService {

    @NonNull
    Optional<Guess> findGuessForToday(@NonNull String user);

    @NonNull
    Optional<Guess> findGuessForDate(@NonNull LocalDate challengeDate, @NonNull String user);

    @NonNull
    Guess recordGuessForToday(@NonNull String user, int guessedCountriesCount);

    @NonNull
    Guess recordGuessForDate(@NonNull LocalDate challengeDate, @NonNull String user, int guessedCountriesCount);

}
