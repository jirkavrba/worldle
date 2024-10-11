package dev.vrba.discord.worldle.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuessSubmitRequestDto(
        @JsonProperty("user") String user,
        @JsonProperty("guessed_countries_count") Integer count
) {
}
