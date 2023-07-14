package hm.zelha.xptracker.util;

import hm.zelha.xptracker.XPTracker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class XPCalculator {
    private static final List<Integer> BASE_LEVEL_XP = readDataFile("base_level_xp")
        .stream()
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    private static final List<Double> XP_MULTIPLIERS = readDataFile("xp_multipliers")
        .stream()
        .map(Double::parseDouble)
        .collect(Collectors.toList());
    private static final List<Double> XP_SUMS = readDataFile("xp_sums")
        .stream()
        .map(Double::parseDouble)
        .collect(Collectors.toList());

    private XPCalculator() {}

    /**
     * Calculates the total XP required to reach a prestige from level 0 prestige 0.
     *
     * @param prestige The prestige to calculate for.
     * @return Total XP required to reach the specified prestige from level 0 prestige 0.
     */
    public static double getTotalPrestigeXP(int prestige) {
        double previousPrestigeRequiredXP;
        if (prestige == 0) {
            previousPrestigeRequiredXP = 0;
        } else {
            previousPrestigeRequiredXP = XP_SUMS.get(prestige - 1);
        }

        return XP_SUMS.get(prestige) - previousPrestigeRequiredXP;
    }

    /**
     * Calculate the needed XP to the specified prestige and level from the previous level.
     *
     * @param prestige The prestige to calculate with.
     * @param level    The level to calculate the needed xp for.
     * @return The needed XP for the specified level and level.
     */
    public static double getNeededXPForLevel(int prestige, int level) {
        return Math.ceil(BASE_LEVEL_XP.get(level / 10) * XP_MULTIPLIERS.get(prestige));
    }

    /**
     * Calculates the XP required to level up from level zero prestige zero to the specified level at the specified prestige.
     *
     * @param prestige The prestige to calculate with.
     * @param level    The level to calculate the total XP up to.
     * @return The total XP required to reach the specified level and prestige.
     */
    public static double getTotalXPForLevel(int prestige, int level) {
        double xp = XP_SUMS.get(prestige);
        for (int i = 120; i >= level; i--) {
            xp -= getNeededXPForLevel(prestige, i);
        }

        return xp;
    }

    /**
     * Calculates the XP required to level up from level 0 and specified prestige to the specified level at the specified prestige.
     *
     * @param prestige The prestige to calculate with.
     * @param level    The level to calculate the total XP up to.
     * @return The XP required to level up from level 0 and specified prestige to the specified level at the specified prestige.
     */
    public static double getTotalXPForLevelAtPrestige(int prestige, int level) {
        return getTotalXPForLevel(prestige, level) - getTotalXPForLevel(prestige, 0);
    }

    @NotNull
    private static List<String> readDataFile(@NotNull String file) {
        @Nullable InputStream stream = XPCalculator.class.getClassLoader().getResourceAsStream("pitdata/" + file);
        if (stream == null) throw new IllegalStateException("Could not find internal data file " + file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = reader.lines().collect(Collectors.toList());

        try {
            reader.close();
        } catch (IOException exception) {
            XPTracker.LOGGER.log(Level.INFO, "Failed to close reader while reading internal data file", exception);
        }

        return lines;
    }
}
