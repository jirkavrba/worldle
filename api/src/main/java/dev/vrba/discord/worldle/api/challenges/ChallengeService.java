package dev.vrba.discord.worldle.api.challenges;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface ChallengeService {

    @NonNull
    Challenge createChallenge(@NonNull LocalDate date);

    @NonNull
    Optional<Challenge> findChallengeByDate(@NonNull LocalDate date);

    @NonNull
    Optional<Challenge> findChallengeForToday();

}
