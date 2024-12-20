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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
public class DailyChallengesModule implements DiscordBotModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyChallengesModule.class);

    private final Clock clock;

    private final TaskScheduler scheduler;

    private final ChallengeService challengeService;

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
        final Instant start = LocalDate.now(clock)
                .atStartOfDay()
                .plusDays(1)
                .plusMinutes(1)
                .toInstant(ZoneOffset.from(OffsetDateTime.now(clock)));

        final Runnable task = () -> {
            final Challenge challenge = challengeService.findOrCreateChallengeForToday();
            final Challenge challengeForYesterday = challengeService.findOrCreateChallengeByDate(challenge.date().minusDays(1));

            LOGGER.info("Sending the daily challenge to subscribed channels.");

            channelService.getSubscribedChannels()
                    .stream()
                    .map(channel -> sendDailyChallenge(client, challenge, challengeForYesterday, channel))
                    .forEach(Mono::subscribe);
        };

        scheduler.scheduleAtFixedRate(task, start, period);

        return Mono.empty();
    }

    @NonNull
    private Mono<Void> sendDailyChallenge(
            final @NonNull GatewayDiscordClient client,
            final @NonNull Challenge challenge,
            final @NonNull Challenge challengeForYesterday,
            final @NonNull SubscribedChannel channel
    ) {
        return client.getChannelById(Snowflake.of(channel.id()))
                .filter(it -> it instanceof TextChannel)
                .cast(TextChannel.class)
                .flatMap(textChannel -> {
                    final EmbedCreateSpec embed = challengeToEmbed(challenge, challengeForYesterday);
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
    private EmbedCreateSpec challengeToEmbed(final @NonNull Challenge challenge, final @NonNull Challenge challengeForYesterday) {
        return EmbedCreateSpec.builder()
                .color(Colors.WORLDLE)
                .title("Daily Worldle challenge")
                .description("Can you guess from which country is the photo below?")
                .addField("Yesterday the answer was", challengeForYesterday.city().getDisplayName(), false)
                .image(challenge.imageUrl())
                .build();
    }
}
