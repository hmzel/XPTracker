package hm.zelha.xptracker.handler;

import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.util.RomanNumerals;
import hm.zelha.xptracker.util.XPCalculator;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
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

    @Getter private String xpString = "";
    @Getter private int prestige = 0;
    @Getter private int level = 0;
    @Getter private int xpToNextLevel = 0;

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

        prestige = RomanNumerals.getIntFromNumeral(prestigeRaw);
        level = Integer.parseInt(levelRaw);

        int newXpToNextLevel;
        if (neededXPRaw.equals("MAXED")) {
            newXpToNextLevel = 0;
        } else {
            newXpToNextLevel = Integer.parseInt(neededXPRaw.replace(",", ""));
        }

        if (xpToNextLevel == newXpToNextLevel) return; // Don't update if the value hasn't changed
        xpToNextLevel = newXpToNextLevel;
        calculate();
    }

    private void calculate() {
        double requiredXPForNextPrestige = XPCalculator.getTotalPrestigeXP(prestige);
        double currentPrestigeXP = XPCalculator.getTotalXPForLevelAtPrestige(prestige, level);
        double neededXPForNextLevel = XPCalculator.getNeededXPForLevel(prestige, level + 1);
        currentPrestigeXP = currentPrestigeXP + neededXPForNextLevel - xpToNextLevel;

        double percent = currentPrestigeXP / requiredXPForNextPrestige;
        xpString = String.format("%.0f/%.0f  %.0f%%", currentPrestigeXP, requiredXPForNextPrestige, percent * 100);
        OverlayRenderer.INSTANCE.update();
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

    private boolean isPlayingPit() {
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
