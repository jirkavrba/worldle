package dev.vrba.discord.worldle.api.service;

import dev.vrba.discord.worldle.api.model.SubscribedChannel;
import org.springframework.lang.NonNull;

import java.util.List;

public interface SubscribedChannelService {

    @NonNull
    List<SubscribedChannel> getSubscribedChannels();

    void subscribe(@NonNull String channel);

    void unsubscribe(@NonNull String channel);

}
