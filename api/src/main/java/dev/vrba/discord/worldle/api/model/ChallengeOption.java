package dev.vrba.discord.worldle.api.model;

import org.springframework.lang.NonNull;

public record ChallengeOption(
    @NonNull Country country,
    boolean correct
) {
}
