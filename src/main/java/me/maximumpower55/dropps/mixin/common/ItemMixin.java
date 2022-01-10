package me.maximumpower55.dropps.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.dropps.common.ItemPhysicsType;
import me.maximumpower55.dropps.server.ExtendedItem;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public abstract class ItemMixin implements ExtendedItem {
    @Unique
    private ItemPhysicsType physicsType;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Item.Properties properties, CallbackInfo info) {
        this.physicsType = ItemPhysicsType.forItem((Item)(Object)this);
    }

    @Override
    public ItemPhysicsType getPhysicsType() {
        return physicsType;
    }
}
