package dev.vrba.discord.worldle.api.repository;

import dev.vrba.discord.worldle.api.model.SubscribedChannel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedChannelRepository extends CrudRepository<SubscribedChannel, String> {
}
