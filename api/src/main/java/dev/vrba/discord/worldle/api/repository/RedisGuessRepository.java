package dev.vrba.discord.worldle.api.repository;

import dev.vrba.discord.worldle.api.model.Guess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RedisGuessRepository extends CrudRepository<Guess, UUID> {

    @NonNull
    Optional<Guess> findByChallengeDateAndUser(@NonNull LocalDate challengeDate, @NonNull String user);
}

