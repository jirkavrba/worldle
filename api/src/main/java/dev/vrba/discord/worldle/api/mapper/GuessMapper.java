package dev.vrba.discord.worldle.api.mapper;

import dev.vrba.discord.worldle.api.dto.GuessDto;
import dev.vrba.discord.worldle.api.model.Guess;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GuessMapper {

    GuessMapper INSTANCE = Mappers.getMapper(GuessMapper.class);

    GuessDto guessToGuessDto(Guess guess);
}
