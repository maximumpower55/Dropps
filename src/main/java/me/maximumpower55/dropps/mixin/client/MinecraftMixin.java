package me.maximumpower55.dropps.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.dropps.DroppsMod;
import me.maximumpower55.dropps.DroppsUtil;
import me.maximumpower55.dropps.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Unique
	private double throwPower = 1.0;

	@Shadow
	@Nullable
	private MultiPlayerGameMode gameMode;

	@Shadow
	@Nullable
	private LocalPlayer player;

	@Shadow
	@Nullable
	private Entity cameraEntity;

	@Shadow
	@Final
	private Options options;

	@Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z", shift = At.Shift.AFTER), cancellable = true)
	private void pickupItem(CallbackInfo ci) {
		if(DroppsMod.getInstance().config.autoPickup || DroppsUtil.raycastItemEntity(cameraEntity, gameMode.getPickRange()).isEmpty()) return;

		ClientPlayNetworking.send(ModNetworking.PICKUP_CHANNEL, PacketByteBufs.empty());
		player.swing(InteractionHand.MAIN_HAND);

		ci.cancel();
	}

	@Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;drop(Z)Z"))
	private boolean dropOrIncreaseThrowPower(LocalPlayer player, boolean fullStack) {
		if(Screen.hasShiftDown()) {
			if(throwPower < 5) throwPower += .078;
			player.displayClientMessage(Component.nullToEmpty("Power: " + (int)throwPower), true);
			return false;
		} else {
			return player.drop(fullStack);
		}
	}

	@Inject(method = "handleKeybinds", at = @At("RETURN"))
	private void dropWithPower(CallbackInfo ci) {
		boolean fullStack = Screen.hasControlDown();

		if(throwPower > 1.0 && !options.keyDrop.isDown()) {
			FriendlyByteBuf buf = PacketByteBufs.create();
			buf.writeInt((int)throwPower);

			ClientPlayNetworking.send(ModNetworking.SET_THROW_POWER_CHANNEL, buf);

			player.drop(fullStack);

			player.swing(InteractionHand.MAIN_HAND);

			throwPower = 1.0;
		}
	}
}
