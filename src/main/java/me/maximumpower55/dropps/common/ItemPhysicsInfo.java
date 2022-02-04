package me.maximumpower55.dropps.common;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public enum ItemPhysicsInfo {
	ITEM(new AABB(-.25, -.25, -.03, .25, .25, .03), 1f),
	BLOCK(new AABB(-.125, -.125, -.125, .125, .125, .125), 2f);

	private final AABB boundingBox;
	private final float mass;

	ItemPhysicsInfo(AABB boundingBox, float mass) {
		this.boundingBox = boundingBox;
		this.mass = mass;
	}

	public AABB getBoundingBox() {
		return boundingBox;
	}

	public float mass() {
		return mass;
	}

	public static ItemPhysicsInfo forItem(Item item) {
		Block block = Registry.BLOCK.get(Registry.ITEM.getKey(item));

		if(block != Blocks.AIR && !block.isPossibleToRespawnInThis()) return BLOCK;

		return ITEM;
	}
}
