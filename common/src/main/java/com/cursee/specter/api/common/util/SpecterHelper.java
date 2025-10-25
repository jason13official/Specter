package com.cursee.specter.api.common.util;

import com.cursee.specter.impl.common.entity.Specter;
import com.cursee.specter.impl.common.item.DyeableCondensedSpecterItem;
import com.cursee.specter.impl.common.registry.ModItems;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SpecterHelper {

  /// Check a 65x8x65 area centered on the livingEntity for a Specter owned by the livingEntity
  public static boolean hasOwnedSpecterNearby(LivingEntity livingEntity) {

    if (livingEntity == null) {
      return false;
    }

    Level level = livingEntity.level();
    boolean foundOwnedSpecter = false;
    List<Specter> specters = level.getNearbyEntities(Specter.class, TargetingConditions.DEFAULT, livingEntity, livingEntity.getBoundingBox().inflate(64.0D, 4D, 64.0D));

    for (Specter specter : specters) {
      if (specter.getOwner() == livingEntity) {
        foundOwnedSpecter = true;

        // stop searching if we found a specter owned by the livingEntity
        break;
      }
    }

    return foundOwnedSpecter;
  }

  /// @param shieldingPlayer if false, defaults to {@link SpecterHelper#hasOwnedSpecterNearby(LivingEntity)}, otherwise checks if a Specter is close enough to shield the livingEntity.
  public static boolean hasOwnedSpecterNearby(Specter specter, LivingEntity livingEntity, boolean shieldingPlayer) {

    if (specter == null || livingEntity == null) {
      return false;
    }

    if (!shieldingPlayer) {
      return SpecterHelper.hasOwnedSpecterNearby(livingEntity);
    }

    return specter.distanceTo(livingEntity) < 4.0f;
  }

  public static ItemStack convertSpecterToCondensedSpecter(Specter specter) {

    ItemStack stack = new ItemStack(ModItems.CONDENSED_SPECTER);

    if (specter.getSpecterColor() != DyeableCondensedSpecterItem.DEFAULT_SPECTER_COLOR) {
      ((DyeableLeatherItem) stack.getItem()).setColor(stack, specter.getSpecterColor());
    }

    if (specter.hasCustomName()) {
      stack.setHoverName(specter.getCustomName());
    }

    return stack;
  }

  public static @Nullable Specter convertCondensedSpecterToOwnedSpecter(LivingEntity living, ItemStack stack) {

    if (!(stack.getItem() instanceof DyeableCondensedSpecterItem)) {
      return null;
    }

    int spawnColor = DyeableCondensedSpecterItem.getColorFromStack(stack);
    Specter specter = new Specter(living.level(), living, spawnColor);

    if (stack.hasCustomHoverName()) {
      specter.setCustomName(stack.getHoverName());
    }

    return specter;
  }
}
