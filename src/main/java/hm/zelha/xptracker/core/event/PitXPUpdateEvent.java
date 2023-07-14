package hm.zelha.xptracker.core.event;

import lombok.AllArgsConstructor;
import net.minecraftforge.fml.common.eventhandler.Event;

@AllArgsConstructor
public class PitXPUpdateEvent extends Event {
    public final int oldPrestige;
    public final int oldLevel;
    public final double oldXPToNextLevel;

    public final int prestige;
    public final int level;
    public final double xpToNextLevel;
}
