package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.Guess;
import dev.vrba.discord.worldle.api.repository.RedisGuessRepository;
import dev.vrba.discord.worldle.api.service.GuessService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisGuessService implements GuessService {

    @NonNull
    private final Clock clock;

    @NonNull
    private final RedisGuessRepository repository;


    @NonNull
    @Override
    public Optional<Guess> findGuessForToday(@NonNull String user) {
        return findGuessForDate(LocalDate.now(clock), user);
    }

    @NonNull
    @Override
    public Optional<Guess> findGuessForDate(@NonNull LocalDate challengeDate, @NonNull String user) {
        return repository.findByChallengeDateAndUser(challengeDate, user);
    }

    @NonNull
    @Override
    public Guess recordGuessForToday(@NonNull String user, int guessedCountriesCount) {
        return recordGuessForDate(LocalDate.now(clock), user, guessedCountriesCount);
    }

    @NonNull
    @Override
    public Guess recordGuessForDate(@NonNull LocalDate challengeDate, @NonNull String user, int guessedCountriesCount) {
        return repository.save(
                new Guess(
                        UUID.randomUUID(),
                        challengeDate,
                        user,
                        guessedCountriesCount
                )
        );
    }

}
