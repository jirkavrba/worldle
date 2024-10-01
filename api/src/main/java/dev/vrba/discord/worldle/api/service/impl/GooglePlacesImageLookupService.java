package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.service.ImageLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class GooglePlacesImageLookupService implements ImageLookupService {

    @NonNull
    private final String key;

    @NonNull
    private final RestClient client = RestClient.create("https://maps.googleapis.com/maps/api/streetview");

    @NonNull
    private final Logger logger = LoggerFactory.getLogger(GooglePlacesImageLookupService.class);

    public GooglePlacesImageLookupService(
            final @NonNull @Value("${integration.google-maps.api-key}") String key
    ) {
        this.key = key;
    }

    @NonNull
    @Override
    public Optional<byte[]> getChallengeImageByCity(@NonNull City city) {
        logger.info("Fetching an image for {}", city.getDisplayName());

        try {
            final byte[] imageData = client.get()
                    .uri(builder ->
                            builder.queryParam("location", city.getDisplayName())
                                    .queryParam("size", "800x600")
                                    .queryParam("key", key)
                                    .build()
                    )
                    .retrieve()
                    .body(byte[].class);

            return Optional.ofNullable(imageData);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
