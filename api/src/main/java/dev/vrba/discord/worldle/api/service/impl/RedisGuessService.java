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

    private final Clock clock;

    private final RedisGuessRepository repository;

    @NonNull
    @Override
    public Optional<Guess> findGuessForToday(final @NonNull String user) {
        return findGuessForDate(LocalDate.now(clock), user);
    }

    @NonNull
    @Override
    public Optional<Guess> findGuessForDate(final @NonNull LocalDate challengeDate, final @NonNull String user) {
        return repository.findByChallengeDateAndUser(challengeDate, user);
    }

    @NonNull
    @Override
    public Guess recordGuessForToday(final @NonNull String user, final int guessedCountriesCount) {
        return recordGuessForDate(LocalDate.now(clock), user, guessedCountriesCount);
    }

    @NonNull
    @Override
    public Guess recordGuessForDate(final @NonNull LocalDate challengeDate, final @NonNull String user, final int guessedCountriesCount) {
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
