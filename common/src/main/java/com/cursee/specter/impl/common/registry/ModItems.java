package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.item.AllayThrottlerItem;
import com.cursee.specter.impl.common.item.SpecterCoreItem;
import com.cursee.specter.impl.common.item.SpecterSummonerItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

  public static Item ALLAY_THROTTLER;
  public static Item THROTTLED_ALLAY_TOTEM;

  public static Item SPECTER_CORE;
  public static Item SPECTER_SUMMONER;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    ALLAY_THROTTLER = new AllayThrottlerItem(new Item.Properties().stacksTo(1));
    THROTTLED_ALLAY_TOTEM = new AllayThrottlerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    SPECTER_CORE = new SpecterCoreItem(new Item.Properties());
    SPECTER_SUMMONER = new SpecterSummonerItem(new Item.Properties());

    consumer.accept(ALLAY_THROTTLER, SpecterCommon.identifier("allay_throttler"));
    consumer.accept(THROTTLED_ALLAY_TOTEM, SpecterCommon.identifier("throttled_allay_totem"));

    consumer.accept(SPECTER_CORE, SpecterCommon.identifier("specter_core"));
    consumer.accept(SPECTER_SUMMONER, SpecterCommon.identifier("specter_summoner"));
  }
}
