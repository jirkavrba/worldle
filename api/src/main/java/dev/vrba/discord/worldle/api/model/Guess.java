package dev.vrba.discord.worldle.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@RedisHash("guesses")
public record Guess(
        @Id @NonNull UUID id,
        @Indexed @NonNull LocalDate challengeDate,
        @Indexed @NonNull String user,
        int guessedCountriesCount
) {
}
