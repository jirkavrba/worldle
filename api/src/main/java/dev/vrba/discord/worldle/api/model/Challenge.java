package dev.vrba.discord.worldle.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RedisHash("challenges")
public record Challenge(
        @Id @NonNull UUID id,
        @Indexed @NonNull LocalDate date,
        @NonNull City city,
        @NonNull String imageUrl,
        @NonNull List<ChallengeOption> options
) {
}

