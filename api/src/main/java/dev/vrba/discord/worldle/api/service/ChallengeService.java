package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.Challenge;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface ChallengeService {

    @NonNull
    Challenge createChallenge(@NonNull LocalDate date);

    @NonNull
    Challenge createChallengeForToday();

    @NonNull
    Optional<Challenge> findChallengeByDate(@NonNull LocalDate date);

    @NonNull
    Optional<Challenge> findChallengeForToday();

    default Challenge findOrCreateChallengeForToday() {
        return findChallengeForToday().orElseGet(this::createChallengeForToday);
    }

}
