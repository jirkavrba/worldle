package dev.vrba.discord.worldle.api.service.impl;

import dev.vrba.discord.worldle.api.model.SubscribedChannel;
import dev.vrba.discord.worldle.api.repository.SubscribedChannelRepository;
import dev.vrba.discord.worldle.api.service.SubscribedChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RedisSubscribedChannelService implements SubscribedChannelService {

    @NonNull
    private final SubscribedChannelRepository repository;

    @NonNull
    @Override
    public List<SubscribedChannel> getSubscribedChannels() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Override
    public void subscribe(@NonNull String channel) {
        if (!repository.existsById(channel)) {
            repository.save(new SubscribedChannel(channel));
        }
    }

    @Override
    public void unsubscribe(@NonNull String channel) {
        repository.deleteById(channel);
    }
}
