package dev.vrba.discord.worldle.api.model;

import org.springframework.lang.NonNull;

public record City(@NonNull String name, @NonNull Country country) {
}
