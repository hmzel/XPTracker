package hm.zelha.xptracker.handlers;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.components.Window;
import hm.zelha.xptracker.ui.PrestigeProgressOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PrestigeProgressRenderer {
    public static final PrestigeProgressRenderer INSTANCE = new PrestigeProgressRenderer();
    private final Window window = new Window(ElementaVersion.V2);
    private final PrestigeProgressOverlay overlay = new PrestigeProgressOverlay();

    private PrestigeProgressRenderer() {
        window.addChild(overlay);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        window.draw();
    }

    public void update() {
        overlay.update();
    }
}
