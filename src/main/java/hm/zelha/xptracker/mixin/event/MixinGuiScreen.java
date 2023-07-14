package hm.zelha.xptracker.mixin.event;

import hm.zelha.xptracker.core.event.GUIMouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void onMouseClicked(CallbackInfo info, int mouseX, int mouseY, int mouseButton) {
        MinecraftForge.EVENT_BUS.post(new GUIMouseEvent.Clicked(mouseX, mouseY, mouseButton));
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseReleased(III)V"
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void onMouseReleased(CallbackInfo info, int mouseX, int mouseY, int mouseButton) {
        MinecraftForge.EVENT_BUS.post(new GUIMouseEvent.Released(mouseX, mouseY, mouseButton));
    }
}
