package dev.vrba.discord.worldle.api.service;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface ImageStorageService {

    @NonNull
    Optional<String> uploadChallengeImage(
            @NonNull byte[] imageData,
            @NonNull LocalDate challengeDate
    );

}
