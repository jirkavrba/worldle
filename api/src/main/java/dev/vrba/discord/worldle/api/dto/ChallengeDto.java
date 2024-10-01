package dev.vrba.discord.worldle.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

public record ChallengeDto(
        @NonNull @JsonProperty("challenge_date") LocalDate date,
        @NonNull @JsonProperty("answer_city_name") String cityName,
        @NonNull @JsonProperty("answer_country_name") String cityCountryName,
        @NonNull @JsonProperty("answer_country_flag") String cityCountryFlag,
        @NonNull @JsonProperty("image_url") String imageUrl,
        @NonNull @JsonProperty("options") List<ChallengeOptionDto> options
) {

}
