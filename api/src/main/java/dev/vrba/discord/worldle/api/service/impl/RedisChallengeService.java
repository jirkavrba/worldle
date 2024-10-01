package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.model.ChallengeOption;
import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.model.Country;
import dev.vrba.discord.worldle.api.repository.RedisChallengeRepository;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import dev.vrba.discord.worldle.api.service.CountryService;
import dev.vrba.discord.worldle.api.service.ImageLookupService;
import dev.vrba.discord.worldle.api.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisChallengeService implements ChallengeService {

    private final Clock clock;

    private final RedisChallengeRepository repository;

    private final CountryService countryService;

    private final ImageLookupService imageLookupService;

    private final ImageStorageService imageStorageService;


    @NonNull
    @Override
    public Challenge createChallengeForToday() {
        return createChallenge(LocalDate.now());
    }

    @NonNull
    @Override
    public Challenge createChallenge(final @NonNull LocalDate date) {
        final List<Country> countries = countryService.getShuffledCountries().subList(0, 16);

        // TODO: Do not use Optional.orElseThrow here...

        final Country country = countries.get(Math.abs(new Random().nextInt()) % countries.size());
        final City city = countryService.getRandomCity(country).orElseThrow();

        final byte[] image = imageLookupService.getChallengeImageByCity(city).orElseThrow();
        final String imageUrl = imageStorageService.uploadChallengeImage(image, date).orElseThrow();

        final List<ChallengeOption> options = countries.stream().map(item ->
                new ChallengeOption(
                        item,
                        item.equals(country)
                )
        ).toList();

        return repository.save(
                new Challenge(
                        UUID.randomUUID(),
                        date,
                        city,
                        imageUrl,
                        options
                )
        );
    }

    @NonNull
    @Override
    public Optional<Challenge> findChallengeByDate(final @NonNull LocalDate date) {
        return repository.findByDate(date);
    }

    @NonNull
    @Override
    public Optional<Challenge> findChallengeForToday() {
        return findChallengeByDate(LocalDate.now(clock));
    }
}
