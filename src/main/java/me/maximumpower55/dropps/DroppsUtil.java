package me.maximumpower55.dropps;

import java.util.Optional;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public enum DroppsUtil {;
	public static Optional<ItemEntity> raycastItemEntity(Entity camera, float reach) {
		Vec3 viewVector = camera.getViewVector(1);

		Vec3 start = camera.getEyePosition();
		Vec3 end = start.add(viewVector.scale(reach));

		final EntityHitResult result = ProjectileUtil.getEntityHitResult(camera, start, end, new AABB(start, end).inflate(1), entity -> entity instanceof ItemEntity, reach * reach);

		return result == null ? Optional.empty() : Optional.of((ItemEntity)result.getEntity());
	}
}
