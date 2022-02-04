package me.maximumpower55.dropps.mixin.client;

import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.dropps.DroppsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.TooltipFlag;

@Mixin(Gui.class)
abstract class GuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private int screenWidth;

	@Shadow
	private int screenHeight;

	@Inject(method = "renderCrosshair", at = @At("HEAD"))
	private void renderItemTooltip(PoseStack poseStack, CallbackInfo ci) {
		Optional<ItemEntity> hit = DroppsUtil.raycastItemEntity(minecraft.getCameraEntity(), 5);

		if(hit.isEmpty()) return;

		ItemEntity itemEntity = hit.get();

		List<Component> tooltip = itemEntity.getItem().getTooltipLines(null, TooltipFlag.Default.NORMAL);

		int line = 0;

		for(Component component : tooltip) {
			minecraft.font.drawShadow(poseStack, component, screenWidth / 2 - minecraft.font.width(component) / 2,  screenHeight / 2 + 15 + line * 10, 0xFFFFFF);

			line++;
		}

		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
		RenderSystem.enableBlend();
	}
}
