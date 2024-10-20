package dev.vrba.discord.worldle.api.discord;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class DiscordBot implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    private final String token;

    private final List<DiscordBotModule> modules;

    public DiscordBot(@Value("${discord.bot.token}") String token, @NonNull List<DiscordBotModule> modules) {
        this.token = Objects.requireNonNull(token);
        this.modules = Objects.requireNonNull(modules);
    }

    @Override
    public void run(ApplicationArguments args) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = Objects.requireNonNull(client.login().block());

        final List<Mono<Void>> moduleRegistrations = modules.stream().map(module -> module.register(gateway)).toList();

        Mono.when(moduleRegistrations)
                .then(gateway.onDisconnect())
                .doOnError(error -> LOGGER.error("Encountered an error while running the discord bot.", error))
                .subscribe();
    }
}
