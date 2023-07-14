package hm.zelha.xptracker.handler;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.components.Window;
import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.core.event.GUIMouseEvent;
import hm.zelha.xptracker.core.event.PitXPUpdateEvent;
import hm.zelha.xptracker.ui.Overlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayRenderer {
    public static final OverlayRenderer INSTANCE = new OverlayRenderer();
    private final Window window = new Window(ElementaVersion.V2);
    private final Overlay overlay = new Overlay();

    private OverlayRenderer() {
        window.addChild(overlay);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (isInactive()) return;
        window.draw();
    }

    @SubscribeEvent
    public void onMouseClick(GUIMouseEvent.Clicked event) {
        if (isInactive()) return;
        window.mouseClick(event.x, event.y, event.button);
    }

    @SubscribeEvent
    public void onMouseRelease(GUIMouseEvent.Released event) {
        if (isInactive()) return;
        window.mouseRelease();
    }

    @SubscribeEvent
    public void onXPUpdate(PitXPUpdateEvent event) {
        overlay.update(event.prestige, event.level, event.xpToNextLevel);
    }

    private boolean isInactive() {
        return !Config.INSTANCE.enabled || (Config.INSTANCE.onlyRenderInPit && !StatTracker.INSTANCE.isPlayingPit());
    }
}
