package hm.zelha.xptracker.handler;

import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.core.event.PitXPUpdateEvent;
import hm.zelha.xptracker.util.RomanNumerals;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatTracker {
    public static final StatTracker INSTANCE = new StatTracker();
    private static final Pattern LEVEL_PATTERN = Pattern.compile("^Level: \\[(?<value>\\d{1,3})]$");
    private static final Pattern PRESTIGE_PATTERN = Pattern.compile("^Prestige: (?<value>\\w+)$");
    private static final Pattern NEEDED_XP_PATTERN = Pattern.compile("^Needed XP: (?<value>[\\d,]+|MAXED)$");

    @Getter private int prestige = 0;
    @Getter private int level = 0;
    @Getter private double xpToNextLevel = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!Config.INSTANCE.enabled) return;
        if (!isPlayingPit()) return;

        @Nullable List<String> lines = getScoreboardLines();
        if (lines == null) return;

        String prestigeRaw = getScoreboardValue(lines, PRESTIGE_PATTERN); // This can be null if the player has not prestiged
        String levelRaw = getScoreboardValue(lines, LEVEL_PATTERN);
        String neededXPRaw = getScoreboardValue(lines, NEEDED_XP_PATTERN);
        if (levelRaw == null || neededXPRaw == null) return;

        int newPrestige = RomanNumerals.getIntFromNumeral(prestigeRaw);
        int newLevel = Integer.parseInt(levelRaw);

        double newXpToNextLevel;
        if (neededXPRaw.equals("MAXED")) {
            newXpToNextLevel = 0;
        } else {
            newXpToNextLevel = Double.parseDouble(neededXPRaw.replace(",", ""));
        }

        if (xpToNextLevel == newXpToNextLevel && prestige == newPrestige && level == newLevel) return;
        MinecraftForge.EVENT_BUS.post(new PitXPUpdateEvent(
            prestige,
            level,
            xpToNextLevel,
            newPrestige,
            newLevel,
            newXpToNextLevel
        ));

        prestige = newPrestige;
        level = newLevel;
        xpToNextLevel = newXpToNextLevel;
    }

    @Nullable
    private String getScoreboardValue(@NotNull List<String> scoreboardLines, @NotNull Pattern pattern) {
        for (String scoreboardLine : scoreboardLines) {
            Matcher matcher = pattern.matcher(scoreboardLine);
            if (matcher.matches()) {
                return matcher.group("value");
            }
        }

        return null;
    }

    @Nullable
    private List<String> getScoreboardLines() {
        @Nullable World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return null;
        @Nullable Scoreboard scoreboard = world.getScoreboard();
        if (scoreboard == null) return null;
        @Nullable ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        if (sidebar == null) return null;

        return scoreboard.getSortedScores(sidebar)
            .stream()
            .map(score -> {
                @Nullable ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
                if (scorePlayerTeam == null) return null;
                String formattedName = scorePlayerTeam.getColorPrefix() + scorePlayerTeam.getColorSuffix();
                return EnumChatFormatting.getTextWithoutFormattingCodes(formattedName);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Checks if the player is currently playing The Pit
     *
     * @return If the player is currently playing The Pit
     */
    public boolean isPlayingPit() {
        @Nullable World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return false;
        @Nullable Scoreboard scoreboard = world.getScoreboard();
        if (scoreboard == null) return false;
        @Nullable ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        if (sidebar == null) return false;

        String name = EnumChatFormatting.getTextWithoutFormattingCodes(sidebar.getDisplayName());
        return name.equals("THE HYPIXEL PIT");
    }
}
