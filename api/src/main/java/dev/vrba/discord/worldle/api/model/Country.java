package dev.vrba.discord.worldle.api.model;

import org.springframework.lang.NonNull;

public record Country(
    @NonNull String name,
    @NonNull String code,
    @NonNull String flag
) {
}
