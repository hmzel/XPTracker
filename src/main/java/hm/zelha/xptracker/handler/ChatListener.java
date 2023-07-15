package hm.zelha.xptracker.handler;

import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.util.XPCalculator;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    private static final Pattern XP_PATTERN = Pattern.compile(".*§b\\+(?<xp>\\d+)XP.*");

    public static double calculatePercent(double currentXP, double requiredXP, int gainedXP, int decimalPlaces) {
        double oldPercent = floorToDecimalPlaces(currentXP / requiredXP * 100, decimalPlaces);
        double newPercent = floorToDecimalPlaces((currentXP + gainedXP) / requiredXP * 100, decimalPlaces);
        return (newPercent * 100 - oldPercent * 100) / 100;
    }

    private static double floorToDecimalPlaces(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.floor(value * factor) / factor;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        StatTracker statTracker = StatTracker.INSTANCE;
        if (!Config.INSTANCE.enabled || !Config.INSTANCE.chatProgression && !statTracker.isPlayingPit()) return;
        Matcher matcher = XP_PATTERN.matcher(event.message.getUnformattedText());
        if (!matcher.matches()) return;

        int xp = Integer.parseInt(matcher.group("xp"));
        double requiredXP;
        double currentXP;

        if (Config.INSTANCE.chatProgressionType == 0) { // 0 = Level
            requiredXP = XPCalculator.getNeededXPForLevel(statTracker.getPrestige(), statTracker.getLevel());
            currentXP = XPCalculator.getNeededXPForLevel(statTracker.getPrestige(), statTracker.getLevel()) - statTracker.getXpToNextLevel();
        } else { // 1 = Prestige
            requiredXP = XPCalculator.getTotalPrestigeXP(statTracker.getPrestige());
            currentXP = XPCalculator.getTotalXPForLevelAtPrestige(statTracker.getPrestige(), statTracker.getLevel(), statTracker.getXpToNextLevel());
        }

        double percent = calculatePercent(currentXP, requiredXP, xp, Config.INSTANCE.chatProgressionDecimalPlaces);

        @SuppressWarnings("MalformedFormatString")
        String levelPercentString = String.format(
            "%." + Config.INSTANCE.chatProgressionDecimalPlaces + "f",
            percent
        );
        event.message.appendText(" §r§7(§b+" + levelPercentString + "%§7)§r");
    }
}
