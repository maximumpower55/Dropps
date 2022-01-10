package me.maximumpower55.dropps.common;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;

public enum ItemPhysicsType {
    ITEM(new AABB(-.25, -.25, -.03, .25, .25, .03), 1f, .1125f),
    BLOCK(new AABB(-.15, -.15, -.15, .15, .15, .15), 2f, .1875f);

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
        if(item instanceof BlockItem) return ItemPhysicsType.BLOCK;

        return ItemPhysicsType.ITEM;
    }
}
