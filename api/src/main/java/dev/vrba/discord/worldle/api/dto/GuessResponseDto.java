package dev.vrba.discord.worldle.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record GuessResponseDto(
        @JsonProperty("has_guessed") boolean guessed,
        @Nullable @JsonProperty("guess") GuessDto guess
) {
}
