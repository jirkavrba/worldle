package dev.vrba.discord.worldle.api.integration.redis;

import dev.vrba.discord.worldle.api.domain.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RedisChallengeRepository extends CrudRepository<Challenge, LocalDate> {
}
