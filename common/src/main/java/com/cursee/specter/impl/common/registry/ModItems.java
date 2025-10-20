package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.item.AllayThrottlerItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

  public static Item ALLAY_THROTTLER;
  public static Item THROTTLED_ALLAY_TOTEM;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    ALLAY_THROTTLER = new AllayThrottlerItem(new Item.Properties().stacksTo(1));
    THROTTLED_ALLAY_TOTEM = new AllayThrottlerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    consumer.accept(ALLAY_THROTTLER, SpecterCommon.identifier("allay_throttler"));
    consumer.accept(THROTTLED_ALLAY_TOTEM, SpecterCommon.identifier("throttled_allay_totem"));
  }
}
