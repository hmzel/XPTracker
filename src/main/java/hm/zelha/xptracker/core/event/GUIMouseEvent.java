package hm.zelha.xptracker.core.event;

import lombok.AllArgsConstructor;
import net.minecraftforge.fml.common.eventhandler.Event;

@AllArgsConstructor
public abstract class GUIMouseEvent extends Event {
    public final int x;
    public final int y;
    public final int button;

    public static class Clicked extends GUIMouseEvent {
        public Clicked(int x, int y, int button) {
            super(x, y, button);
        }
    }

    public static class Released extends GUIMouseEvent {
        public Released(int x, int y, int button) {
            super(x, y, button);
        }
    }
}
