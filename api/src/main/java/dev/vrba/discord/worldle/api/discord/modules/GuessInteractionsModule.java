package dev.vrba.discord.worldle.api.discord.modules;

import dev.vrba.discord.worldle.api.discord.Colors;
import dev.vrba.discord.worldle.api.discord.DiscordBotModule;
import dev.vrba.discord.worldle.api.discord.Emojis;
import dev.vrba.discord.worldle.api.model.Challenge;
import dev.vrba.discord.worldle.api.model.ChallengeOption;
import dev.vrba.discord.worldle.api.model.Guess;
import dev.vrba.discord.worldle.api.service.ChallengeService;
import dev.vrba.discord.worldle.api.service.GuessService;
import dev.vrba.discord.worldle.api.service.SubscribedChannelService;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GuessInteractionsModule implements DiscordBotModule {

    private final GuessService guessService;

    private final ChallengeService challengeService;

    private final SubscribedChannelService channelService;

    private final Map<LocalDate, Map<String, PartialGuess>> cache = new HashMap<>();

    private record PartialGuess(@NonNull Set<String> guessedCountries) {
        public PartialGuess() {
            this(Collections.emptySet());
        }

        public int guessNumber() {
            return guessedCountries.size() + 1;
        }
    }

    public GuessInteractionsModule(
            final @NonNull GuessService guessService,
            final @NonNull ChallengeService challengeService,
            final @NonNull SubscribedChannelService channelService
    ) {
        this.guessService = guessService;
        this.challengeService = challengeService;
        this.channelService = channelService;
    }

    @NonNull
    @Override
    public Mono<Void> register(final @NonNull GatewayDiscordClient client) {
        return client.on(ButtonInteractionEvent.class).flatMap(this::handleButtonInteraction).then();
    }

    @NonNull
    private Mono<Void> handleButtonInteraction(final @NonNull ButtonInteractionEvent event) {
        final String id = event.getCustomId();

        return switch (id) {
            case "guess" -> handleInitialGuess(event);
            case String guess when id.startsWith("guess:") -> handleCountryGuess(guess.substring("guess:".length()), event);
            default -> Mono.empty();
        };
    }

    @NonNull
    private Mono<Void> handleInitialGuess(final @NonNull ButtonInteractionEvent event) {
        final String user = event.getInteraction().getUser().getId().asString();
        final Challenge challenge = challengeService.findOrCreateChallengeForToday();
        final Optional<Guess> guess = guessService.findGuessForDate(challenge.date(), user);

        if (guess.isPresent()) {
            return event.reply()
                    .withEphemeral(true)
                    .withEmbeds(
                            EmbedCreateSpec.builder()
                                    .color(Colors.WORLDLE)
                                    .title("You have already guessed today.")
                                    .description("Wait for the next challenge that will be posted tomorrow.")
                                    .build()
                    );
        }

        final PartialGuess partialGuess = findPartialGuess(challenge.date(), user);
        final EmbedCreateSpec embed = challengeToEmbed(challenge, partialGuess);
        final List<LayoutComponent> buttons = challengeToButtons(challenge, partialGuess);

        return event.reply()
                .withEmbeds(embed)
                .withComponents(buttons)
                .withEphemeral(true);
    }

    @NonNull
    private Mono<Void> handleCountryGuess(
            final @NonNull String guessedCountry,
            final @NonNull ButtonInteractionEvent event
    ) {
        final String user = event.getInteraction().getUser().getId().asString();
        final Challenge challenge = challengeService.findOrCreateChallengeForToday();
        final PartialGuess partialGuess = findPartialGuess(challenge.date(), user);

        final Optional<ChallengeOption> wrappedOption = challenge.options()
                .stream()
                .filter(item -> item.country().code().equals(guessedCountry))
                .findFirst();

        if (wrappedOption.isEmpty()) {
            return Mono.empty();
        }

        final ChallengeOption option = wrappedOption.get();

        if (option.correct()) {
            final Guess guess = guessService.recordGuessForToday(user, partialGuess.guessNumber());

            return event.edit(
                    InteractionApplicationCommandCallbackSpec.builder()
                            .addEmbed(challengeToAnswerEmbed(challenge, partialGuess))
                            .addAllComponents(List.of())
                            .build()
            ).then(sendGuessToSubscribedChannels(guess, event));
        }

        final Set<String> updatedGuessedCountries = new HashSet<>(partialGuess.guessedCountries);
        updatedGuessedCountries.add(option.country().code());

        final PartialGuess updatedPartialGuess = updatePartialGuess(challenge.date(), user, updatedGuessedCountries);

        return event.edit(
                InteractionApplicationCommandCallbackSpec.builder()
                        .addEmbed(challengeToEmbed(challenge, updatedPartialGuess))
                        .addAllComponents(challengeToButtons(challenge, updatedPartialGuess))
                        .build()
        );
    }

    @NonNull
    private Mono<Void> sendGuessToSubscribedChannels(
            final @NonNull Guess guess,
            final @NonNull ButtonInteractionEvent event
    ) {
        final User user = event.getInteraction().getUser();
        final EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .author(

                        user.getGlobalName().orElse(user.getUsername()) + " found the answer!",
                        null,
                        user.getAvatarUrl()
                )
                .description(buildGuessBar(guess.guessedCountriesCount()))
                .build();

        return Flux.fromIterable(channelService.getSubscribedChannels())
                .flatMap(channel -> event.getClient().getChannelById(Snowflake.of(channel.id())))
                .filter(channel -> channel instanceof TextChannel)
                .cast(TextChannel.class)
                .flatMap(channel -> channel.createMessage(embed))
                .then();
    }

    @NonNull
    private PartialGuess findPartialGuess(
            final @NonNull LocalDate date,
            final @NonNull String user
    ) {
        performCacheCleanup(date);

        cache.putIfAbsent(date, new ConcurrentHashMap<>());
        cache.get(date).putIfAbsent(user, new PartialGuess());

        return cache.get(date).get(user);
    }

    @NonNull
    private PartialGuess updatePartialGuess(
            final @NonNull LocalDate date,
            final @NonNull String user,
            final @NonNull Set<String> guessedCountries
    ) {
        final PartialGuess updatedGuess = new PartialGuess(guessedCountries);

        cache.putIfAbsent(date, new ConcurrentHashMap<>());
        cache.get(date).put(user, updatedGuess);

        return updatedGuess;
    }

    private void performCacheCleanup(final @NonNull LocalDate date) {
        final List<LocalDate> obsoleteKeys = cache.keySet()
                .stream()
                .filter(key -> !key.isEqual(date))
                .toList();

        obsoleteKeys.forEach(cache::remove);
    }

    @NonNull
    private EmbedCreateSpec challengeToEmbed(
            final @NonNull Challenge challenge,
            final @NonNull PartialGuess guess
    ) {
        return EmbedCreateSpec.builder()
                .color(Colors.WORLDLE)
                .image(challenge.imageUrl())
                .title("Guess the country")
                .description("Use buttons below to submit your guesses.")
                .addField("Guess", buildGuessBar(guess.guessedCountries.size() + 1), false)
                .build();
    }


    @NonNull
    private EmbedCreateSpec challengeToAnswerEmbed(
            final @NonNull Challenge challenge,
            final @NonNull PartialGuess guess
    ) {
        return EmbedCreateSpec.builder()
                .color(Colors.DISCORD_GREEN)
                .image(challenge.imageUrl())
                .title("You found the answer!")
                .description("The exact city on the picture is **" + challenge.city().getDisplayName() + "**")
                .addField("Guess", buildGuessBar(guess.guessedCountries.size() + 1), false)
                .build();
    }

    @NonNull
    private List<LayoutComponent> challengeToButtons(
            final @NonNull Challenge challenge,
            final @NonNull PartialGuess guess
    ) {
        return IntStream.range(0, 4)
                .mapToObj(base -> {
                    final List<Button> buttons = IntStream.range(0, 4)
                            .map(index -> base * 4 + index)
                            .mapToObj(index -> {
                                final ChallengeOption option = challenge.options().get(index);
                                final String code = option.country().code();
                                final boolean disabled = guess.guessedCountries().contains(code);

                                return Button.secondary("guess:" + code, option.country().asString()).disabled(disabled);
                            })
                            .toList();

                    return ActionRow.of(buttons);
                })
                .map(it -> (LayoutComponent) it)
                .toList();
    }

    @NonNull
    private String buildGuessBar(final int guessNumber) {
        return IntStream.rangeClosed(1, 16)
                .mapToObj(it -> it == guessNumber ? Emojis.guessNumber(it) : Emojis.guessNumberDisabled(it))
                .map(ReactionEmoji.Custom::asFormat)
                .collect(Collectors.joining(" "));
    }
}
