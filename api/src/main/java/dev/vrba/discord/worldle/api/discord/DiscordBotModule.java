package dev.vrba.discord.worldle.api.discord;

import discord4j.core.GatewayDiscordClient;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface DiscordBotModule {
    @NonNull
    Mono<Void> register(final @NonNull GatewayDiscordClient client);
}
