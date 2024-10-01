package dev.vrba.discord.worldle.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record ChallengeOptionDto(
        @NonNull @JsonProperty("country_name") String countryName,
        @NonNull @JsonProperty("country_flag") String countryFlag,
        @NonNull @JsonProperty("is_correct") Boolean correct
) {
}
