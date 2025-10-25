package com.cursee.specter;

import java.util.function.BiFunction;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class SpecterClient {

  public static final BiFunction<ItemStack, Integer, Integer> DYED_LEATHER_COLOR_FN;

  static {
    DYED_LEATHER_COLOR_FN = (itemStack, i) -> i > 0 ? -1 : ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack); // ? still causes original behavior or uses override?
  }

  public static void init() {
  }
}
