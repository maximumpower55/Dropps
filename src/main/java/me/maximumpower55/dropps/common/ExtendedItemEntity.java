package me.maximumpower55.dropps.common;

import net.minecraft.world.entity.item.ItemEntity;

public interface ExtendedItemEntity {
	ItemPhysicsInfo getPhysicsInfo();
	void invokeTryToMerge(ItemEntity itemEntity);
}
