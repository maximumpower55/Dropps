package me.maximumpower55.dropps.mixin.server;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.mojang.math.Quaternion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.collision.space.MinecraftSpace;
import dev.lazurite.rayon.impl.bullet.math.Convert;
import dev.lazurite.toolbox.api.math.QuaternionHelper;
import me.maximumpower55.dropps.DroppsMod;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
abstract class PlayerMixin extends Entity {
    private PlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
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

            MinecraftSpace.get(level).getWorkerThread().execute(() -> {
                rigidBody.setLinearVelocity(Convert.toBullet(viewVector.scale(DroppsMod.getConfig().throwVelocity)));
                rigidBody.setAngularVelocity(new Vector3f(random.nextInt(10) - 5, random.nextInt(10) - 5, random.nextInt(10) - 5));
                rigidBody.setPhysicsRotation(Convert.toBullet(orientation));
            });
        }
    }
}
