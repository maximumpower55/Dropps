package me.maximumpower55.dropps.mixin.server;

import com.jme3.math.Vector3f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.lazurite.rayon.api.EntityPhysicsElement;
import dev.lazurite.rayon.impl.bullet.collision.body.entity.EntityRigidBody;
import dev.lazurite.rayon.impl.bullet.collision.body.shape.MinecraftShape;
import dev.lazurite.rayon.impl.bullet.collision.space.MinecraftSpace;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import me.maximumpower55.dropps.DroppsMod;
import me.maximumpower55.dropps.common.ItemPhysicsType;
import me.maximumpower55.dropps.server.ExtendedItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements EntityPhysicsElement {
    @Shadow abstract ItemStack getItem();

    @Shadow abstract void tryToMerge(ItemEntity itemEntity);

    @Unique
    private final EntityRigidBody rigidBody = new EntityRigidBody(this);

    private ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<?> entityType, Level level, CallbackInfo ci) {
        ItemPhysicsType physicsType = ((ExtendedItem)getItem().getItem()).getPhysicsType();

        MinecraftSpace.get(level).getWorkerThread().execute(() -> {
            rigidBody.setCollisionShape(MinecraftShape.of(physicsType.aabb()));
            rigidBody.setMass(physicsType.mass());
        });
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    private void tick(CallbackInfo info) {
        Vector3f location = rigidBody.getPhysicsLocation(new Vector3f());

        setPos(location.x, location.y + getBoundingBox().getYsize() * .5f, location.z);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private void setDeltaMovementInTick0(ItemEntity self, Vec3 velocity) { }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 1))
    private void setDeltaMovementInTick1(ItemEntity self, Vec3 velocity) { }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 2))
    private void setDeltaMovementInTick2(ItemEntity self, Vec3 velocity) { }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"))
    private boolean noCollision(Level self, Entity entity, AABB aabb) {
        return true;
    }

    @Inject(method = "isMergable", at = @At("RETURN"), cancellable = true)
    private void isMergable(CallbackInfoReturnable<Boolean> cir) {
        if(!DroppsMod.getConfig().itemMerge) cir.setReturnValue(false);
    }

    @Inject(method = "setUnderwaterMovement", at = @At("HEAD"), cancellable = true)
    private void setUnderwaterMovement(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "setUnderLavaMovement", at = @At("HEAD"), cancellable = true)
    private void setUnderLavaMovement(CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    public EntityRigidBody getRigidBody() {
        return rigidBody;
    }
}
