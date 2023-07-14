package hm.zel.xptracker.util;

import hm.zelha.xptracker.util.XPCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XPCalculatorTests {
    @Test
    void testTotalPrestigeXP() {
        assertEquals(65950, XPCalculator.getTotalPrestigeXP(0));
        assertEquals(85750, XPCalculator.getTotalPrestigeXP(3));
    }

    @Test
    void testTotalXPForLevel() {
        assertEquals(1725030, XPCalculator.getTotalXPForLevel(12, 55));
        assertEquals(138510, XPCalculator.getTotalXPForLevel(1, 120));
        assertEquals(495023, XPCalculator.getTotalXPForLevel(6, 11));
        assertEquals(7150, XPCalculator.getTotalXPForLevel(0, 62));
        assertEquals(17526330, XPCalculator.getTotalXPForLevel(25, 120));
    }

    @Test
    void testTotalXPForLevelAtPrestige() {
        assertEquals(24900, XPCalculator.getTotalXPForLevelAtPrestige(12, 54));
    }

    @Test
    void testTotalXPForLevelAtPrestigeNeededXP() {
        assertEquals(48297, XPCalculator.getTotalXPForLevelAtPrestige(3, 98, 1053));
    }

    @Test
    void testNeededXPForLevel() {
        assertEquals(1300, XPCalculator.getNeededXPForLevel(3, 99));
        assertEquals(27, XPCalculator.getNeededXPForLevel(6, 3));
        assertEquals(1300, XPCalculator.getNeededXPForLevel(3, 99));
        assertEquals(1300, XPCalculator.getNeededXPForLevel(3, 99));
    }
}
