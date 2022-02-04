package me.maximumpower55.dropps.mixin.server;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.mojang.authlib.GameProfile;
import com.mojang.math.Quaternion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.collision.space.MinecraftSpace;
import dev.lazurite.rayon.impl.bullet.math.Convert;
import dev.lazurite.toolbox.api.math.QuaternionHelper;
import me.maximumpower55.dropps.DroppsMod;
import me.maximumpower55.dropps.common.ExtendedPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin extends Player implements ExtendedPlayer {
	@Unique
	private int throwPower = 1;

	private ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
		super(level, blockPos, f, gameProfile);
	}

	@Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
	private void drop(ItemStack droppedItem, boolean dropAround, boolean traceItem, CallbackInfoReturnable<ItemEntity> cir) {
		ItemEntity itemEntity = cir.getReturnValue();

		if(itemEntity != null) {
			Vec3 viewVector = getViewVector(1);

			Vec3 normalizedViewVector = viewVector.normalize();
			Vec3 forwardPosition = itemEntity.position().add(normalizedViewVector.scale(.25)).add(0, itemEntity.getEyeHeight(), 0);

			itemEntity.setPos(forwardPosition.x, forwardPosition.y - .3d, forwardPosition.z);

			PhysicsRigidBody rigidBody = ((EntityPhysicsElement)itemEntity).getRigidBody();

			Quaternion orientation = Convert.toMinecraft(new com.jme3.math.Quaternion());
			QuaternionHelper.rotateX(orientation, random.nextInt(180));
			QuaternionHelper.rotateY(orientation, random.nextInt(180));
			QuaternionHelper.rotateZ(orientation, random.nextInt(180));

			double throwVelocity = DroppsMod.getInstance().config.throwVelocity * throwPower;

			MinecraftSpace.get(level).getWorkerThread().execute(() -> {
				rigidBody.setLinearVelocity(Convert.toBullet(viewVector.scale(throwVelocity)));
				rigidBody.setAngularVelocity(new Vector3f(random.nextInt(10) - 5, random.nextInt(10) - 5, random.nextInt(10) - 5));
				rigidBody.setPhysicsRotation(Convert.toBullet(orientation));
			});

			throwPower = 1;
		}
	}

	@Override
	public void setThrowPower(int value) {
		throwPower = value;
	}
}
