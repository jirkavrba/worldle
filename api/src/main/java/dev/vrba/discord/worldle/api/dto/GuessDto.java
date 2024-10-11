package dev.vrba.discord.worldle.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public record GuessDto(
        @NonNull @JsonProperty("challenge_date") LocalDate date,
        @NonNull @JsonProperty("user_id") String user,
        @NonNull @JsonProperty("guessed_countries_count") Integer guessedCountriesCount
) {
}
