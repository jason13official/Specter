package com.cursee.specter.impl.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpecterCoreItem extends Item {

  public SpecterCoreItem() {
    super(new Properties().stacksTo(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

    if (!entity.isOnFire()) {
      entity.setSecondsOnFire(1);
    }

    super.inventoryTick(stack, level, entity, slotId, isSelected);
  }
}
