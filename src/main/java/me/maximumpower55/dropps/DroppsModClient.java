package me.maximumpower55.dropps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class DroppsModClient implements ClientModInitializer {
    public static final Minecraft MC = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        // TODO: Custom Dropped Item Rendering
    }
}
