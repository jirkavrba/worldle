package dev.vrba.discord.worldle.api.service;

import org.springframework.lang.NonNull;

public interface ImageStorageService {

    @NonNull
    String uploadImage(@NonNull byte[] data);

}
