package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.City;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ImageLookupService {

    @NonNull
    Optional<byte[]> getChallengeImageByCity(@NonNull City city);

}
