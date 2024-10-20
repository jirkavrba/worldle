package dev.vrba.discord.worldle.api.discord.modules;

import dev.vrba.discord.worldle.api.discord.Colors;
import dev.vrba.discord.worldle.api.discord.DiscordBotModule;
import dev.vrba.discord.worldle.api.discord.Emojis;
import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.model.SubscribedChannel;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import dev.vrba.discord.worldle.api.service.SubscribedChannelService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
public class DailyChallengesModule implements DiscordBotModule {

    @NonNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyChallengesModule.class);

    @NonNull
    private final Clock clock;

    @NonNull
    private final TaskScheduler scheduler;

    @NonNull
    private final ChallengeService challengeService;

    @NonNull
    private final SubscribedChannelService channelService;

    public DailyChallengesModule(
            final @NonNull Clock clock,
            final @NonNull TaskScheduler scheduler,
            final @NonNull ChallengeService challengeService,
            final @NonNull SubscribedChannelService channelService
    ) {
        this.clock = clock;
        this.scheduler = scheduler;
        this.challengeService = challengeService;
        this.channelService = channelService;
    }

    @NonNull
    @Override
    public Mono<Void> register(final @NonNull GatewayDiscordClient client) {
        Objects.requireNonNull(client);

        LOGGER.info("Scheduling a recurring task for sending out daily challenges.");

        final Duration period = Duration.ofDays(1);
        final Instant midnight = LocalDate.now(clock).atStartOfDay()
                .plusDays(1)
                .plusMinutes(1)
                .toInstant(ZoneOffset.UTC);

        final Runnable task = () -> {
            final Challenge challenge = challengeService.findOrCreateChallengeForToday();

            LOGGER.info("Sending the daily challenge to subscribed channels.");

            channelService.getSubscribedChannels()
                    .stream()
                    .map(channel -> sendDailyChallenge(client, challenge, channel))
                    .forEach(Mono::subscribe);
        };

        scheduler.scheduleAtFixedRate(task, midnight, period);

        return Mono.empty();
    }

    @NonNull
    private Mono<Void> sendDailyChallenge(
            final @NonNull GatewayDiscordClient client,
            final @NonNull Challenge challenge,
            final @NonNull SubscribedChannel channel
    ) {
        return client.getChannelById(Snowflake.of(channel.id()))
                .filter(it -> it instanceof TextChannel)
                .cast(TextChannel.class)
                .flatMap(textChannel -> {
                    final EmbedCreateSpec embed = challengeToEmbed(challenge);
                    final LayoutComponent button = ActionRow.of(
                            Button.secondary("guess", Emojis.WORLDLE_LOGO, "Take a guess")
                    );

                    final MessageCreateSpec message = MessageCreateSpec.builder()
                            .addEmbed(embed)
                            .addComponent(button)
                            .build();

                    return textChannel.createMessage(message);
                })
                .then();
    }

    @NonNull
    private EmbedCreateSpec challengeToEmbed(final @NonNull Challenge challenge) {
        return EmbedCreateSpec.builder()
                .color(Colors.WORLDLE)
                .title("Daily Worldle challenge")
                .description("Can you guess from which country is the photo below?")
                .image(challenge.imageUrl())
                .footer("Good luck", null)
                .build();
    }
}
