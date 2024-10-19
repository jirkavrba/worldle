package dev.vrba.discord.worldle.api.discord.modules;

import dev.vrba.discord.worldle.api.discord.DiscordBotModule;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ApplicationCommandsModule implements DiscordBotModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationCommandsModule.class);

    @NonNull
    @Override
    public Mono<Void> register(@NonNull GatewayDiscordClient client) {
        return registerSlashCommands(client)
                .then(registerSlashCommandHandlers(client));
    }

    @NonNull
    private Mono<Void> registerSlashCommands(@NonNull GatewayDiscordClient client) {
        LOGGER.info("Registering application slash commands");

        final RestClient rest = client.getRestClient();
        final List<ApplicationCommandRequest> requests = List.of(
                ApplicationCommandRequest.builder()
                        .name("register")
                        .description("Registers the channel to daily Worldle challenges")
                        .dmPermission(false)
                        .build(),
                ApplicationCommandRequest.builder()
                        .name("unregister")
                        .description("Unregisters the channel from daily Worldle challenges")
                        .dmPermission(false)
                        .build()
        );

        return rest.getApplicationId()
                .flatMapMany(id -> rest.getApplicationService().bulkOverwriteGlobalApplicationCommand(id, requests))
                .map(it -> {
                    LOGGER.info("Registered the [/{}] command under id {}", it.name(), it.id());
                    return it;
                })
                .then();
    }

    @NonNull
    private Mono<Void> registerSlashCommandHandlers(@NonNull GatewayDiscordClient client) {
        LOGGER.info("Registering application command handlers");

        return client.on(ChatInputInteractionEvent.class)
                .flatMap(event -> {
                    event.getInteraction()
                            .getCommandInteraction();
                    return Mono.empty();
                })
                .then();
    }
}
