package dev.vrba.discord.worldle.api.discord;

import discord4j.core.object.reaction.ReactionEmoji;

import java.util.List;

public class Emojis {

    public static final ReactionEmoji WORLDLE_LOGO = ReactionEmoji.of(1297445120253366354L, "worldle", false);

    private static final ReactionEmoji GUESS_1_DISABLED = ReactionEmoji.Custom.of(1297512450345668638L, "guess_01_disabled", false);

    private static final ReactionEmoji GUESS_2_DISABLED = ReactionEmoji.Custom.of(1297512481933099080L, "guess_02_disabled", false);

    private static final ReactionEmoji GUESS_3_DISABLED = ReactionEmoji.Custom.of(1297512944434937936L, "guess_03_disabled", false);

    private static final ReactionEmoji GUESS_4_DISABLED = ReactionEmoji.Custom.of(1297512958620074006L, "guess_04_disabled", false);

    private static final ReactionEmoji GUESS_5_DISABLED = ReactionEmoji.Custom.of(1297512968250195980L, "guess_05_disabled", false);

    private static final ReactionEmoji GUESS_6_DISABLED = ReactionEmoji.Custom.of(1297512977330868246L, "guess_06_disabled", false);

    private static final ReactionEmoji GUESS_7_DISABLED = ReactionEmoji.Custom.of(1297512986520457236L, "guess_07_disabled", false);

    private static final ReactionEmoji GUESS_8_DISABLED = ReactionEmoji.Custom.of(1297512999912865825L, "guess_08_disabled", false);

    private static final ReactionEmoji GUESS_9_DISABLED = ReactionEmoji.Custom.of(1297513038857109556L, "guess_09_disabled", false);

    private static final ReactionEmoji GUESS_10_DISABLED = ReactionEmoji.Custom.of(1297513046557593610L, "guess_10_disabled", false);

    private static final ReactionEmoji GUESS_11_DISABLED = ReactionEmoji.Custom.of(1297513053641904178L, "guess_11_disabled", false);

    private static final ReactionEmoji GUESS_12_DISABLED = ReactionEmoji.Custom.of(1297513060663300097L, "guess_12_disabled", false);

    private static final ReactionEmoji GUESS_13_DISABLED = ReactionEmoji.Custom.of(1297513069878181929L, "guess_13_disabled", false);

    private static final ReactionEmoji GUESS_14_DISABLED = ReactionEmoji.Custom.of(1297513082532265984L, "guess_14_disabled", false);

    private static final ReactionEmoji GUESS_15_DISABLED = ReactionEmoji.Custom.of(1297513094926438421L, "guess_15_disabled", false);

    private static final ReactionEmoji GUESS_16_DISABLED = ReactionEmoji.Custom.of(1297513105403809884L, "guess_16_disabled", false);

    private static final ReactionEmoji GUESS_1 = ReactionEmoji.Custom.of(1297513318293966869L, "guess_01", false);

    private static final ReactionEmoji GUESS_2 = ReactionEmoji.Custom.of(1297513327227961445L, "guess_02", false);

    private static final ReactionEmoji GUESS_3 = ReactionEmoji.Custom.of(1297513337600475167L, "guess_03", false);

    private static final ReactionEmoji GUESS_4 = ReactionEmoji.Custom.of(1297513350397431808L, "guess_04", false);

    private static final ReactionEmoji GUESS_5 = ReactionEmoji.Custom.of(1297513362061660261L, "guess_05", false);

    private static final ReactionEmoji GUESS_6 = ReactionEmoji.Custom.of(1297513371436060725L, "guess_06", false);

    private static final ReactionEmoji GUESS_7 = ReactionEmoji.Custom.of(1297513391568584776L, "guess_07", false);

    private static final ReactionEmoji GUESS_8 = ReactionEmoji.Custom.of(1297513400225628190L, "guess_08", false);

    private static final ReactionEmoji GUESS_9 = ReactionEmoji.Custom.of(1297513410350547035L, "guess_09", false);

    private static final ReactionEmoji GUESS_10 = ReactionEmoji.Custom.of(1297513422157778954L, "guess_10", false);

    private static final ReactionEmoji GUESS_11 = ReactionEmoji.Custom.of(1297513432635019325L, "guess_11", false);

    private static final ReactionEmoji GUESS_12 = ReactionEmoji.Custom.of(1297513441925271572L, "guess_12", false);

    private static final ReactionEmoji GUESS_13 = ReactionEmoji.Custom.of(1297513452524273725L, "guess_13", false);

    private static final ReactionEmoji GUESS_14 = ReactionEmoji.Custom.of(1297513465694388316L, "guess_14", false);

    private static final ReactionEmoji GUESS_15 = ReactionEmoji.Custom.of(1297513476335599656L, "guess_15", false);

    private static final ReactionEmoji GUESS_16 = ReactionEmoji.Custom.of(1297513487177875457L, "guess_16", false);

    public static ReactionEmoji.Custom guessNumber(int number) {
        if (number < 1 || number > 16) {
            throw new IllegalArgumentException("Guess number must be between 1 and 16");
        }

        return List.of(
                        GUESS_1, GUESS_2, GUESS_3, GUESS_4,
                        GUESS_5, GUESS_6, GUESS_7, GUESS_8,
                        GUESS_9, GUESS_10, GUESS_11, GUESS_12,
                        GUESS_13, GUESS_14, GUESS_15, GUESS_16
                )
                .get(number - 1)
                .asCustomEmoji()
                .orElseThrow();
    }

    public static ReactionEmoji.Custom guessNumberDisabled(int number) {
        if (number < 1 || number > 16) {
            throw new IllegalArgumentException("Guess number must be between 1 and 16");
        }

        return List.of(
                        GUESS_1_DISABLED, GUESS_2_DISABLED, GUESS_3_DISABLED, GUESS_4_DISABLED,
                        GUESS_5_DISABLED, GUESS_6_DISABLED, GUESS_7_DISABLED, GUESS_8_DISABLED,
                        GUESS_9_DISABLED, GUESS_10_DISABLED, GUESS_11_DISABLED, GUESS_12_DISABLED,
                        GUESS_13_DISABLED, GUESS_14_DISABLED, GUESS_15_DISABLED, GUESS_16_DISABLED
                )
                .get(number - 1)
                .asCustomEmoji()
                .orElseThrow();
    }
}
