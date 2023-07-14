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
    }
}
