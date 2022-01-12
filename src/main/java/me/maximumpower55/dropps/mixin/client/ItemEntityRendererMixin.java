package me.maximumpower55.dropps.mixin.client;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.math.Convert;
import me.maximumpower55.dropps.common.ItemPhysicsType;
import me.maximumpower55.dropps.server.ExtendedItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;

@Environment(EnvType.CLIENT)
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
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
    private void init(Context context, CallbackInfo ci) {
        this.shadowRadius = 0;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(ItemEntity entity, float entityYaw, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        ItemStack itemStack = entity.getItem();

        final int seed = itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) * entity.getId();
        random.setSeed(seed);

        poseStack.pushPose();

        final BakedModel bakedModel = itemRenderer.getModel(itemStack, entity.level, null, seed);

        final Quaternion orientation = Convert.toMinecraft(((EntityPhysicsElement)entity).getPhysicsRotation(new com.jme3.math.Quaternion(), tickDelta));

        final int renderAmount = getRenderAmount(itemStack);

        final ItemPhysicsType itemPhysicsType = ((ExtendedItem)itemStack.getItem()).getPhysicsType();

        final boolean hasDepth = itemPhysicsType == ItemPhysicsType.BLOCK && bakedModel.isGui3d();

        final ItemTransform transform = bakedModel.getTransforms().ground;

        poseStack.mulPose(orientation);
        poseStack.translate(0, -itemPhysicsType.offset(), 0);

        poseStack.translate(0, 0, ((.09375 - (renderAmount * .1)) * .5));

        for(int i = 0; i < renderAmount; ++i) {
            poseStack.pushPose();

            if(i > 0)
                if(hasDepth) {
                    float xOffset = (random.nextFloat() * 2f - 1f) * .15f;
                    float yOffset = (random.nextFloat() * 2f - 1f) * .15f;
                    float zOffset = (random.nextFloat() * 2f - 1f) * .15f;

                    poseStack.translate(xOffset, yOffset, zOffset);
                } else {
                    poseStack.translate(0, .125, 0);
                    poseStack.mulPose(Vector3f.ZP.rotation((this.random.nextFloat() - .5f)));
                    poseStack.translate(0, -.125, 0);
                }

            poseStack.mulPose(new Quaternion(transform.rotation.x(), transform.rotation.y(), transform.rotation.z(), true));

            poseStack.scale(1.25f, 1.25f, 1.25f);

            itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, false, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, bakedModel);

            poseStack.popPose();

            if(!hasDepth) {
                poseStack.translate(0, 0, .1);
            }
        }

        poseStack.popPose();

        ci.cancel();
    }
}
