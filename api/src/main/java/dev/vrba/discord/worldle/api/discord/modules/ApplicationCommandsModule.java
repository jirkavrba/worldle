package dev.vrba.discord.worldle.api.discord.modules;

import dev.vrba.discord.worldle.api.discord.DiscordBotModule;
import dev.vrba.discord.worldle.api.service.SubscribedChannelService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class ApplicationCommandsModule implements DiscordBotModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationCommandsModule.class);

    private final static String SUBSCRIBE_COMMAND = "subscribe";

    private final static String UNSUBSCRIBE_COMMAND = "unsubscribe";

    @NonNull
    private final SubscribedChannelService service;

    public ApplicationCommandsModule(SubscribedChannelService service) {
        this.service = Objects.requireNonNull(service);
    }

    @NonNull
    @Override
    public Mono<Void> register(@NonNull GatewayDiscordClient client) {
        Objects.requireNonNull(client);

        return registerSlashCommands(client)
                .then(registerSlashCommandHandlers(client));
    }

    @NonNull
    private Mono<Void> registerSlashCommands(@NonNull GatewayDiscordClient client) {
        LOGGER.info("Registering application slash commands");

        final RestClient rest = client.getRestClient();
        final List<ApplicationCommandRequest> requests = List.of(
                ApplicationCommandRequest.builder()
                        .name(SUBSCRIBE_COMMAND)
                        .description("Subscribes the channel to daily Worldle challenges")
                        .dmPermission(false)
                        .build(),
                ApplicationCommandRequest.builder()
                        .name(UNSUBSCRIBE_COMMAND)
                        .description("Unsubscribes the channel from daily Worldle challenges")
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
                .flatMap(event ->
                        switch (event.getCommandName()) {
                            case SUBSCRIBE_COMMAND -> handleSubscribeCommand(event);
                            case UNSUBSCRIBE_COMMAND -> handleUnsubscribeCommand(event);
                            default -> Mono.empty();
                        }
                )
                .then();
    }

    private Mono<Void> handleSubscribeCommand(final ChatInputInteractionEvent event) {
        service.subscribe(event.getInteraction().getChannelId().toString());

        return event.reply(
                InteractionApplicationCommandCallbackSpec.builder()
                        .addEmbed(
                                EmbedCreateSpec.builder()
                                        .color(Color.of(0x57F287))
                                        .title("Channel subscribed")
                                        .description(
                                                """
                                                        This channel has been subscribed to daily worldle challenges.
                                                        Challenges are usually posted at midnight CEST.
                                                        
                                                        To unsubscribe, use the `/unsubscribe` command.
                                                        
                                                        Thank you for playing.
                                                        """)
                                        .build()
                        )
                        .build()
        );
    }

    private Mono<Void> handleUnsubscribeCommand(final ChatInputInteractionEvent event) {
        service.unsubscribe(event.getInteraction().getChannelId().toString());

        return event.reply(
                InteractionApplicationCommandCallbackSpec.builder()
                        .addEmbed(
                                EmbedCreateSpec.builder()
                                        .color(Color.of(0xED4245))
                                        .title("Channel unsubscribed")
                                        .description(
                                                """
                                                        This channel will no longer receive the daily Worldle challenges.
                                                        
                                                        You can always subscribe back by using the `/subscribe` command.
                                                        """)
                                        .build()
                        )
                        .build()
        );
    }
}
