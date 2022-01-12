package me.maximumpower55.dropps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;

public class DroppsMod implements ModInitializer {
    public static final String MOD_ID = "dropps";

    private static ModConfig config = null;

    public static final Logger LOGGER = LogManager.getLogger(DroppsMod.class);

    @Override
    public void onInitialize() {
        ModConfig.register();
    }

    public static ModConfig getConfig() {
        if(config == null) config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        return config;
    }
}
