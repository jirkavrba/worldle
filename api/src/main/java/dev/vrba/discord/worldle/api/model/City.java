package dev.vrba.discord.worldle.api.model;

import org.springframework.lang.NonNull;

public record City(@NonNull String name, @NonNull Country country) {

    @NonNull
    public String getDisplayName() {
        return String.format("%s, %s", name, country.name());
    }
}
