package dev.vrba.discord.worldle.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

@RedisHash("challenges")
public record Challenge(
    @Id @NonNull LocalDate date,
    @NonNull String city,
    @NonNull String imageUrl,
    @NonNull List<ChallengeOption> options
) {
}

