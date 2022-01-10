package me.maximumpower55.dropps.mixin.common.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.item.ItemEntity;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
    @Invoker(value = "isMergable")
    boolean invokeIsMergable();
}
