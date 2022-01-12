package me.maximumpower55.dropps;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.TranslatableComponent;

public class DroppsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig config = DroppsMod.getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(() -> ModConfig.save())
                .setParentScreen(parent)
                .setTitle(new TranslatableComponent("title.dropps.config"));

            ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("category.dropps.general"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startDoubleField(new TranslatableComponent("option.dropps.throwVelocity"), config.throwVelocity)
                .setDefaultValue(1.5)
                .setTooltip(new TranslatableComponent(""))
                .setSaveConsumer(newValue -> config.throwVelocity = newValue)
                .build());

            general.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.dropps.itemMerge"), config.itemMerge)
                .setDefaultValue(false)
                .setTooltip(new TranslatableComponent(""))
                .setSaveConsumer(newValue -> config.itemMerge = newValue)
                .build());

            return builder.build();
        };
    }
}
