package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.model.ChallengeOption;
import dev.vrba.discord.worldle.api.model.City;
import dev.vrba.discord.worldle.api.model.Country;
import dev.vrba.discord.worldle.api.repository.RedisChallengeRepository;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisChallengeService implements ChallengeService {

    private final Clock clock;

    private final RedisChallengeRepository repository;

    @NonNull
    @Override
    public Challenge createChallenge(final @NonNull LocalDate date) {
        return repository.save(
            new Challenge(
                date,
                new City(
                    "TODO: City name",
                    new Country(
                        "TODO: Country name",
                        "TODO: Country code",
                        "TODO: Country flag"
                    )
                ),
                "TODO: Fetch and upload an image and get back a public URL",
                List.of(
                    new ChallengeOption(
                        new Country(
                            "TODO: Country name",
                            "TODO: Country code",
                            "TODO: Country flag"
                        ),
                        true
                    )
                )
            )
        );
    }

    @NonNull
    @Override
    public Optional<Challenge> findChallengeByDate(final @NonNull LocalDate date) {
        return Optional.empty();
    }

    @NonNull
    @Override
    public Optional<Challenge> findChallengeForToday() {
        return findChallengeByDate(LocalDate.now(clock));
    }
}
