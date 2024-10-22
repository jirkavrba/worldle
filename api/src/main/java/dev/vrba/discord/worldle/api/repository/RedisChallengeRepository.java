package dev.vrba.discord.worldle.api.repository;

import dev.vrba.discord.worldle.api.model.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RedisChallengeRepository extends CrudRepository<Challenge, UUID> {

    @NonNull
    Optional<Challenge> findByDate(final @NonNull LocalDate date);

}
