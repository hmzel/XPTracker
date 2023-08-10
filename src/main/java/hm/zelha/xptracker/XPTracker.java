package hm.zelha.xptracker;

import hm.zelha.xptracker.command.ConfigCommand;
import hm.zelha.xptracker.core.Config;
import hm.zelha.xptracker.handler.ChatListener;
import hm.zelha.xptracker.handler.OverlayRenderer;
import hm.zelha.xptracker.handler.StatTracker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.logging.Logger;

@Mod(
    modid = XPTracker.MOD_ID,
    name = XPTracker.MOD_NAME,
    version = XPTracker.VERSION
)
public class XPTracker {
    public static final String MOD_ID = "xptracker";
    public static final String MOD_NAME = "XPTracker";
    public static final String VERSION = "1.0.1";
    public static final Logger LOGGER = Logger.getLogger(MOD_NAME);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Config.INSTANCE.preload();
        new ConfigCommand().register();

        MinecraftForge.EVENT_BUS.register(StatTracker.INSTANCE);
        MinecraftForge.EVENT_BUS.register(OverlayRenderer.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new ChatListener());
    }
}
