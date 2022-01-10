package me.maximumpower55.dropps.mixin.server;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.collision.space.MinecraftSpace;
import dev.lazurite.rayon.impl.bullet.math.Convert;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {
    private static final Vector3f DROP_VELOCITY = new Vector3f().multLocal(1.25f);

    private PlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
    private void drop(ItemStack droppedItem, boolean dropAround, boolean traceItem, CallbackInfoReturnable<ItemEntity> cir) {
        ItemEntity itemEntity = cir.getReturnValue();

        if(itemEntity != null) {
            Vec3 viewVector = getViewVector(1f);

            Vec3 normalizedViewVector = viewVector.normalize();
            Vec3 forwardPosition = itemEntity.position().add(normalizedViewVector.multiply(new Vec3(.25d, .25d, .25d))).add(0d, itemEntity.getEyeHeight(), 0d);

            itemEntity.setPos(forwardPosition.x, forwardPosition.y - .3d, forwardPosition.z);

            PhysicsRigidBody rigidBody = ((EntityPhysicsElement)itemEntity).getRigidBody();

            MinecraftSpace.get(level).getWorkerThread().execute(() -> {
                rigidBody.setLinearVelocity(Convert.toBullet(viewVector.multiply(new Vec3(1.5d, 1.5d, 1.5d))).multLocal(DROP_VELOCITY));
                rigidBody.setAngularVelocity(new Vector3f(random.nextInt(10) - 5, random.nextInt(10) - 5, random.nextInt(10) - 5));
            });
        }
    }
}
