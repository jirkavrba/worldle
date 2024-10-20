package dev.vrba.discord.worldle.api.mapper;

import dev.vrba.discord.worldle.api.dto.ChallengeDto;
import dev.vrba.discord.worldle.api.dto.ChallengeOptionDto;
import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.model.ChallengeOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "city.country.name", target = "cityCountryName")
    @Mapping(source = "city.country.flag", target = "cityCountryFlag")
    ChallengeDto challengeToChallengeDto(Challenge source);

    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "country.flag", target = "countryFlag")
    ChallengeOptionDto challengeOptionToChallengeOptionDto(ChallengeOption source);
}
