package hm.zelha.xptracker.handler;

import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.util.XPCalculator;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
    private static final Pattern XP_PATTERN = Pattern.compile(".*§b\\+(?<xp>\\d+)XP.*");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        StatTracker statTracker = StatTracker.INSTANCE;
        if (!Config.INSTANCE.enabled || !Config.INSTANCE.chatProgression && !statTracker.isPlayingPit()) return;
        Matcher matcher = XP_PATTERN.matcher(event.message.getUnformattedText());
        if (!matcher.matches()) return;

        int xp = Integer.parseInt(matcher.group("xp"));
        double currentLevelRequiredXP = XPCalculator.getNeededXPForLevel(statTracker.getPrestige(), statTracker.getLevel());
        double percent = xp / currentLevelRequiredXP;

        @SuppressWarnings("MalformedFormatString")
        String levelPercentString = String.format(
            "%." + Config.INSTANCE.chatProgressionDecimalPlaces + "f",
            percent * 100
        );
        event.message.appendText(" §r§7(§b+" + levelPercentString + "%§7)§r");
    }
}
