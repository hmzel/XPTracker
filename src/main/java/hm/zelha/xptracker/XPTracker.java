package hm.zelha.xptracker;

import hm.zelha.xptracker.commands.ConfigCommand;
import hm.zelha.xptracker.core.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = XPTracker.MOD_ID,
    name = XPTracker.MOD_NAME,
    version = XPTracker.VERSION
)
public class XPTracker {
    public static final String MOD_ID = "xptracker";
    public static final String MOD_NAME = "XPTracker";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Config.INSTANCE.preload();
        new ConfigCommand().register();
    }
}
