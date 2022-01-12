package me.maximumpower55.dropps;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = DroppsMod.MOD_ID)
public class ModConfig implements ConfigData {
    private static ConfigHolder<ModConfig> holder;

    @ConfigEntry.Gui.Tooltip()
    public double throwVelocity = 1.5;

    @ConfigEntry.Gui.Tooltip()
    public boolean itemMerge = false;

    public static void register() {
        DroppsMod.LOGGER.info("Registering Config");

        holder = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        DroppsMod.LOGGER.info("Loading Config");
    }

    public static void save() {
        DroppsMod.LOGGER.info("Saving Config");

        holder.save();
    }
}
