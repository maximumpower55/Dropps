package me.maximumpower55.dropps;

import java.util.Optional;

import me.maximumpower55.dropps.common.ExtendedPlayer;
import me.maximumpower55.dropps.config.DroppsConfig;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public enum ModNetworking {;
	public static final ResourceLocation PICKUP_CHANNEL = DroppsMod.id("pickup");
	public static final ResourceLocation SET_THROW_POWER_CHANNEL = DroppsMod.id("set_throw_power");

	public static void register(DroppsConfig config) {
		if(!config.autoPickup) {
			ServerPlayNetworking.registerGlobalReceiver(PICKUP_CHANNEL, (server, player, handler, buf, responseSender) -> {
				server.execute(() -> {
					Optional<ItemEntity> hit = DroppsUtil.raycastItemEntity(player.getCamera(), 6);

					if(hit.isEmpty()) return;

					ItemEntity itemEntity = hit.get();

					ItemStack itemStack = itemEntity.getItem().copy();

					if(player.getInventory().add(itemStack)) {
						player.take(itemEntity, itemStack.getCount());
						itemEntity.discard();
					}
				});
			});
		}

		ServerPlayNetworking.registerGlobalReceiver(SET_THROW_POWER_CHANNEL, (server, player, handler, buf, responseSender) -> {
			int power = buf.readInt();

			server.execute(() -> {
				((ExtendedPlayer)player).setThrowPower(power);
			});
		});
	}
}
