package dev.vrba.discord.worldle.api.challenges.domain;

import org.springframework.lang.NonNull;

public record ChallengeOption(
    @NonNull String countryName,
    @NonNull String countryFlag,
    boolean isCorrect
) {}
