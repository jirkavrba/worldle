package dev.vrba.discord.worldle.api.challenges;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RedisHash("challenges")
public record Challenge(
    @Id @NonNull UUID id,
    @NonNull LocalDate date,
    @NonNull String city,
    @NonNull String imageUrl,
    @NonNull List<ChallengeOption> options
) {
}

