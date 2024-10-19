package dev.vrba.discord.worldle.api.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.service.ImageLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GooglePlacesImageLookupService implements ImageLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GooglePlacesImageLookupService.class);

    private final String key;

    private final RestClient client = RestClient.create("https://maps.googleapis.com/maps/api/place");

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GooglePlacesApiResponse(@NonNull @JsonProperty("candidates") List<GooglePlacesApiCandidate> candidates) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GooglePlacesApiCandidate(@NonNull @JsonProperty("photos") List<GooglePlacesApiPhoto> photos) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GooglePlacesApiPhoto(@NonNull @JsonProperty("photo_reference") String reference) {
    }

    public GooglePlacesImageLookupService(
            final @NonNull @Value("${integration.google-maps.api-key}") String key
    ) {
        this.key = Objects.requireNonNull(key);
    }

    @NonNull
    @Override
    public Optional<byte[]> getChallengeImageByCity(@NonNull City city) {
        LOGGER.info("Fetching an image for {}", city.getDisplayName());

        try {
            return getRandomPhotoReferenceByCity(city).flatMap(this::getImageDataByPhotoReference);
        } catch (Exception exception) {
            LOGGER.error("There was an error while fetching the image.", exception);
            return Optional.empty();
        }
    }

    private Optional<String> getRandomPhotoReferenceByCity(@NonNull City city) {
        final GooglePlacesApiResponse response = client.get()
                .uri(builder ->
                        builder
                                .path("/findplacefromtext/json")
                                .queryParam("input", city.getDisplayName())
                                .queryParam("inputtype", "textquery")
                                .queryParam("fields", "name,photos")
                                .queryParam("key", key)
                                .build()
                )
                .retrieve()
                .body(GooglePlacesApiResponse.class);

        return Optional
                .ofNullable(response)
                .flatMap(payload -> {
                    final List<String> references = payload.candidates.stream()
                            .flatMap(it -> it.photos.stream())
                            .map(it -> it.reference)
                            .collect(Collectors.toList());

                    Collections.shuffle(references);

                    return references.stream().findFirst();
                });
    }

    private Optional<byte[]> getImageDataByPhotoReference(@NonNull String reference) {
        final String redirect = client.get()
                .uri(builder ->
                        builder
                                .path("/photo")
                                .queryParam("photoreference", reference)
                                .queryParam("maxwidth", "800")
                                .queryParam("maxhidth", "600")
                                .queryParam("key", key)
                                .build()
                )
                .retrieve()
                .toBodilessEntity()
                .getHeaders()
                .getFirst("Location");

        final byte[] imageData = client.get()
                .uri(Objects.requireNonNull(redirect))
                .retrieve()
                .body(byte[].class);

        return Optional.ofNullable(imageData);
    }
}
