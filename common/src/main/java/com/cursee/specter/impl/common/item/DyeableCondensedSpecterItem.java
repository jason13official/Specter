package com.cursee.specter.impl.common.item;

import com.cursee.specter.api.common.util.SpecterHelper;
import com.cursee.specter.impl.common.entity.Specter;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DyeableCondensedSpecterItem extends SpecterCoreItem implements DyeableLeatherItem {

  public static int DEFAULT_SPECTER_COLOR = 0xFFFFFFFF;

  public DyeableCondensedSpecterItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getColor(ItemStack stack) {
    return DyeableCondensedSpecterItem.getColorFromStack(stack);
  }

  public static int getColorFromStack(ItemStack stack) {
    CompoundTag compoundtag = stack.getTagElement(DyeableLeatherItem.TAG_DISPLAY);
    return compoundtag != null && compoundtag.contains(DyeableLeatherItem.TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? compoundtag.getInt(DyeableLeatherItem.TAG_COLOR) : DEFAULT_SPECTER_COLOR;
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

    AtomicBoolean foundSpecterOwnedBySelf = new AtomicBoolean();
    level.getNearbyEntities(Specter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D)).forEach(rawSpecter -> {
      if (rawSpecter.getOwner() == player) {
        foundSpecterOwnedBySelf.set(true);
      }
    });

    if (!foundSpecterOwnedBySelf.get()) {

      if (!player.level().isClientSide()) {

        Specter specterToSpawn = SpecterHelper.convertCondensedSpecterToOwnedSpecter(player, player.getItemInHand(usedHand));

        if (specterToSpawn != null) {
          specterToSpawn.moveTo(player.position());
          player.level().addFreshEntity(specterToSpawn);
        }
      }

      player.setItemInHand(usedHand, ItemStack.EMPTY);
    }

    return super.use(level, player, usedHand);
  }
}
