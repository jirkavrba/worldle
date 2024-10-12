package dev.vrba.discord.worldle.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;

@RedisHash("subscribed_channels")
public record SubscribedChannel(
        @Id @NonNull String id
) {
}
