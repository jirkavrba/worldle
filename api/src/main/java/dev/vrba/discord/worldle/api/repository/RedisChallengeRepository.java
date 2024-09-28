package dev.vrba.discord.worldle.api.repository;

import dev.vrba.discord.worldle.api.model.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RedisChallengeRepository extends CrudRepository<Challenge, LocalDate> {
}
