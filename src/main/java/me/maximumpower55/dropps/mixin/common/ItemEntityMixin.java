package me.maximumpower55.dropps.mixin.common;

import com.simsilica.mathd.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.collision.body.EntityRigidBody;
import dev.lazurite.rayon.impl.bullet.collision.body.shape.MinecraftShape;
import dev.lazurite.rayon.impl.bullet.collision.space.MinecraftSpace;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import me.maximumpower55.dropps.DroppsMod;
import me.maximumpower55.dropps.common.ExtendedItemEntity;
import me.maximumpower55.dropps.common.ItemPhysicsInfo;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity implements EntityPhysicsElement, ExtendedItemEntity {
	@Shadow abstract ItemStack getItem();

	@Shadow abstract void tryToMerge(ItemEntity itemEntity);

	@Unique
	private final EntityRigidBody rigidBody = new EntityRigidBody(this);

	@Unique
	private Item prevItem = Items.AIR;

	@Unique
	private ItemPhysicsInfo physicsInfo = ItemPhysicsInfo.ITEM;

	private ItemEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
	private void tick(CallbackInfo ci) {
		if(!getItem().getItem().equals(prevItem)) {
			physicsInfo = ItemPhysicsInfo.forItem(getItem().getItem());

			MinecraftSpace.get(level).getWorkerThread().execute(() -> {
				rigidBody.setCollisionShape(MinecraftShape.convex(physicsInfo.getBoundingBox()));
				rigidBody.setMass(physicsInfo.mass());
			});

			prevItem = getItem().getItem();
		}

		Vec3d location = rigidBody.getPhysicsLocationDp(new Vec3d());

		setPos(location.x, location.y, location.z);
	}

	@Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
	private void disableAutoPickup(Player player, CallbackInfo ci) {
		if(!DroppsMod.getInstance().config.autoPickup) {
			ci.cancel();
		}
	}

	@Inject(method = "isMergable", at = @At("RETURN"), cancellable = true)
	private void isMergable(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}

	@Override
	public EntityRigidBody getRigidBody() {
		return rigidBody;
	}

	@Override
	public ItemPhysicsInfo getPhysicsInfo() {
		return physicsInfo;
	}

	@Override
	public void invokeTryToMerge(ItemEntity itemEntity) {
		tryToMerge(itemEntity);
	}
}
