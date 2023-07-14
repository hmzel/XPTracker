package hm.zelha.xptracker.handler;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.components.Window;
import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.core.event.GUIMouseEvent;
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
        if (!Config.INSTANCE.enabled) return;
        window.draw();
    }

    @SubscribeEvent
    public void onMouseClick(GUIMouseEvent.Clicked event) {
        if (!Config.INSTANCE.enabled) return;
        window.mouseClick(event.x, event.y, event.button);
    }

    @SubscribeEvent
    public void onMouseRelease(GUIMouseEvent.Released event) {
        if (!Config.INSTANCE.enabled) return;
        window.mouseRelease();
    }

    public void update() {
        overlay.update();
    }
}
