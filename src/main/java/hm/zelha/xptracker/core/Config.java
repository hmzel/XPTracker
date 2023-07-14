package hm.zelha.xptracker.core;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {
    public static final Config INSTANCE = new Config();

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "Whether or not the mod is enabled.",
        category = "General"
    )
    public boolean enabled = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Only render in Pit",
        description = "Whether or not the overlay should only be shown in pit.",
        category = "General"
    )
    public boolean onlyRenderInPit = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Prestige Progression",
        description = "Whether or not the overlay should show prestige progression.",
        category = "General"
    )
    public boolean prestigeProgression = true;

    @Property(
        type = PropertyType.TEXT,
        name = "Prestige Progression Format",
        description = "The format of the prestige progression text.\n" +
            "Formatting codes can be used.\n" +
            "Variables: %prestige%, %prestige_roman%, %prestige_xp%, %prestige_xp_needed%, %prestige_xp_progress%.",
        category = "General"
    )
    public String prestigeProgressionFormat = "&7[&f%prestige_roman%&7] &a%prestige_xp%&7/&b%prestige_xp_needed% &7(&d%prestige_xp_progress%%&7)";


    @Property(
        type = PropertyType.SWITCH,
        name = "Level Progression",
        description = "Whether or not the overlay should show level progression.",
        category = "General"
    )
    public boolean levelProgression = true;

    @Property(
        type = PropertyType.TEXT,
        name = "Level Progression Format",
        description = "The format of the level progression text.\n" +
            "Formatting codes can be used.\n" +
            "Variables: %level%, %level_xp%, %level_xp_needed%, %level_xp_progress%.",
        category = "General"
    )
    public String levelProgressionFormat = "&7[&f%level%&7] &a%level_xp%&7/&b%level_xp_needed% &7(&d%level_xp_progress%%&7)";

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "X",
        category = "General",
        hidden = true
    )
    public float x = 8;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Y",
        category = "General",
        hidden = true
    )
    public float y = 8;

    public Config() {
        super(new File("./config/xptracker.toml"));
        initialize();
    }
}
