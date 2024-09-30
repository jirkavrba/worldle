package dev.vrba.discord.worldle.api.service.impl;

import com.uploadcare.api.Client;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import dev.vrba.discord.worldle.api.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadcareImageStorageService implements ImageStorageService {

    @NonNull
    private final Client client;

    @NonNull
    private final Logger logger = LoggerFactory.getLogger(UploadcareImageStorageService.class);

    @NonNull
    private static final String CDN_URL_BASE = "https://www.ucarecdn.com";

    @NonNull
    @Override
    public Optional<String> uploadChallengeImage(@NonNull byte[] imageData, @NonNull LocalDate challengeDate) {
        final String filename = challengeDate.format(DateTimeFormatter.BASIC_ISO_DATE) + ".png";
        final Uploader uploader = new FileUploader(client, imageData, filename);

        try {
            final String url = CDN_URL_BASE + uploader.upload().cdnPath().build();

            logger.info("Uploaded challenge image to the following URL: {}", url);

            return Optional.of(url);
        } catch (UploadFailureException exception) {
            logger.error("There was an error while uploading challenge image: ", exception);
            return Optional.empty();
        }
    }
}