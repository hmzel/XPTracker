package hm.zel.xptracker.handler;

import hm.zelha.xptracker.handler.ChatListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatListenerTests {
    @Test
    void testCalculatePercent() {
        assertEquals(0.01, ChatListener.calculatePercent(55008, 100000, 3, 2));
        assertEquals(0.00, ChatListener.calculatePercent(48880, 85750, 2, 2));
    }
}
