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

    private final Client client;

    private final Logger logger = LoggerFactory.getLogger(UploadcareImageStorageService.class);

    @NonNull
    @Override
    public Optional<String> uploadChallengeImage(final @NonNull byte[] imageData, final @NonNull LocalDate challengeDate) {
        final String filename = challengeDate.format(DateTimeFormatter.BASIC_ISO_DATE) + ".png";
        final Uploader uploader = new FileUploader(client, imageData, filename);

        try {
            final String url = uploader.upload().getOriginalFileUrl().toString();

            logger.info("Uploaded challenge image to the following URL: {}", url);

            return Optional.of(url);
        } catch (UploadFailureException exception) {
            logger.error("There was an error while uploading challenge image: ", exception);
            return Optional.empty();
        }
    }
}
