package me.maximumpower55.dropps.mixin.client;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.math.Convert;
import me.maximumpower55.dropps.common.ItemPhysicsInfo;
import me.maximumpower55.dropps.common.ExtendedItemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

@Environment(EnvType.CLIENT)
@Mixin(ItemEntityRenderer.class)
abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
	@Shadow
	@Final
	private Random random;

	@Shadow
	@Final
	private ItemRenderer itemRenderer;

	@Shadow
	abstract int getRenderAmount(ItemStack itemStack);

	private ItemEntityRendererMixin(Context context) {
		super(context);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void disableShadow(Context context, CallbackInfo ci) {
		this.shadowRadius = 0;
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(ItemEntity entity, float entityYaw, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
		ItemStack itemStack = entity.getItem();

		final int seed = itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) * itemStack.getDamageValue();
		random.setSeed(seed);

		poseStack.pushPose();

		final BakedModel bakedModel = itemRenderer.getModel(itemStack, entity.level, null, seed);

		final Quaternion orientation = Convert.toMinecraft(((EntityPhysicsElement)entity).getPhysicsRotation(new com.jme3.math.Quaternion(), tickDelta));

		final int renderAmount = getRenderAmount(itemStack);

		final ItemPhysicsInfo itemPhysicsInfo = ((ExtendedItemEntity)entity).getPhysicsInfo();

		final boolean hasDepth = itemPhysicsInfo == ItemPhysicsInfo.BLOCK && bakedModel.isGui3d();

		final ItemTransform transform = bakedModel.getTransforms().ground;

		final float scaleX = transform.scale.x();
		final float scaleY = transform.scale.y();
		final float scaleZ = transform.scale.z();

		poseStack.mulPose(orientation);

		if(!hasDepth) {
			double r = -.0 * (renderAmount - 1) * .5 * scaleX;
			double s = -.0 * (renderAmount - 1) * .5 * scaleY;
			double t = -.09375 * (renderAmount - 1) * .5 * scaleZ;

			poseStack.translate(r, s, t);
		}

		for(int i = 0; i < renderAmount; ++i) {
			poseStack.pushPose();

			if(i > 0)
				if(hasDepth) {
					double xOffset = (random.nextDouble() * 2 - 1) * .15;
					double yOffset = (random.nextDouble() * 2 - 1) * .15;
					double zOffset = (random.nextDouble() * 2 - 1) * .15;

					poseStack.translate(xOffset, yOffset, zOffset);
				} else {
					double xOffset = (random.nextDouble() * 2 - 1) * .15 * .5;
					double yOffset = (random.nextDouble() * 2 - 1) * .15 * .5;

					poseStack.translate(xOffset, yOffset, 0);
				}

			poseStack.mulPose(new Quaternion(transform.rotation.x(), transform.rotation.y(), transform.rotation.z(), true));
			poseStack.scale(scaleX, scaleY, scaleZ);

			itemRenderer.render(itemStack, ItemTransforms.TransformType.NONE, false, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, bakedModel);

			poseStack.popPose();

			if(!hasDepth) {
				poseStack.translate(0, 0, .1 * scaleZ);
			}
		}

		poseStack.popPose();

		super.render(entity, entityYaw, tickDelta, poseStack, buffer, light);

		ci.cancel();
	}
}
