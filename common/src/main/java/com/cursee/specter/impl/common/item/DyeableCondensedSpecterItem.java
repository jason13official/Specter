package com.cursee.specter.impl.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DyeableCondensedSpecterItem extends SpecterCoreItem implements DyeableLeatherItem {

  public int DEFAULT_SPECTER_COLOR = 0xFFFFFFFF;

  public DyeableCondensedSpecterItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getColor(ItemStack stack) {
    CompoundTag compoundtag = stack.getTagElement(DyeableLeatherItem.TAG_DISPLAY);
    return compoundtag != null && compoundtag.contains(DyeableLeatherItem.TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? compoundtag.getInt(DyeableLeatherItem.TAG_COLOR) : DEFAULT_SPECTER_COLOR;
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
