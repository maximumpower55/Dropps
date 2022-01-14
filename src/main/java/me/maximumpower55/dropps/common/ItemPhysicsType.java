package me.maximumpower55.dropps.common;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public enum ItemPhysicsType {
    ITEM(new AABB(-.25, -.25, -.03, .25, .25, .03), 1.0f, 0.1125f),
    BLOCK(new AABB(-.125, -.125, -.125, .125, .125, .125), 2.0f, 0.1875f);

    private final AABB aabb;
    private final float mass;
    private final float offset;

    ItemPhysicsType(AABB aabb, float mass, float offset) {
        this.aabb = aabb;
        this.mass = mass;
        this.offset = offset;
    }

    public AABB aabb() {
        return aabb;
    }

    public float mass() {
        return mass;
    }

    public float offset() {
        return offset;
    }

    public static ItemPhysicsType forItem(Item item) {
        Block block = Registry.BLOCK.get(Registry.ITEM.getKey(item));

        if(block != Blocks.AIR) return BLOCK;

        return ITEM;
    }
}
