package hm.zelha.xptracker.command;

import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import hm.zelha.xptracker.core.Config;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("xptracker");
    }

    @DefaultHandler
    public void handle() {
        EssentialAPI.getGuiUtil().openScreen(Config.INSTANCE.gui());
    }
}
