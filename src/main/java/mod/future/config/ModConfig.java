package mod.future.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import mod.future.Main;

public class ModConfig extends Config {

    @Switch(name = "AutoF", description = "Automatically accepts friend requests", size = 2)
    public static boolean autoF = false;

    @Switch(name = "AutoGuild", description = "Automatically sends a message to your Guild Members", size = 2)
    public static boolean autoG = false;


    public ModConfig() {
        super(new Mod(Main.NAME, ModType.UTIL_QOL), Main.MODID + ".json");
        initialize();
    }

}
