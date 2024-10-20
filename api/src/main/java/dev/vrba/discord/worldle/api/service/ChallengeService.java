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
    Challenge regenerateChallenge(@NonNull LocalDate date);

    @NonNull
    Optional<Challenge> findChallengeByDate(@NonNull LocalDate date);

    @NonNull
    Optional<Challenge> findChallengeForToday();

    @NonNull
    default Challenge findOrCreateChallengeForToday() {
        return findChallengeForToday().orElseGet(this::createChallengeForToday);
    }

    @NonNull
    default Challenge findOrCreateChallengeByDate(@NonNull LocalDate date) {
        return findChallengeByDate(date).orElseGet(() -> createChallenge(date));
    }

}
