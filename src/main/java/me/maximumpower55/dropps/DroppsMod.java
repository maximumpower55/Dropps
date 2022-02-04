package me.maximumpower55.dropps;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.api.event.collision.ElementCollisionEvents;
import me.maximumpower55.dropps.common.ExtendedItemEntity;
import me.maximumpower55.dropps.config.DroppsConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public class DroppsMod implements ModInitializer {
	public static final String MOD_ID = "dropps";

	private static DroppsMod INSTANCE;

	public final DroppsConfig config = new DroppsConfig(this);

	public final Logger logger = LoggerFactory.getLogger("Dropps");

	@Override
	public void onInitialize() {
		INSTANCE = this;

		logger.info("Physics very cool. you wouldn't exist without it");

		config.load();

		ModNetworking.register(config);

		ElementCollisionEvents.ELEMENT_COLLISION.register((body1, body2, impulse) -> {
			if(config.itemMerge && body1 instanceof ItemEntity && body2 instanceof ItemEntity) {
				ItemEntity item1 = ((ItemEntity)((EntityPhysicsElement)body1).cast());
				ItemEntity item2 = ((ItemEntity)((EntityPhysicsElement)body2).cast());
				Level level = item1.getLevel();

				if(!level.isClientSide()) {
					((ExtendedItemEntity)item1).invokeTryToMerge(item2);
				}
			}
		});
	}

	public static ResourceLocation id(@NotNull String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static DroppsMod getInstance() {
		return INSTANCE;
	}
}
