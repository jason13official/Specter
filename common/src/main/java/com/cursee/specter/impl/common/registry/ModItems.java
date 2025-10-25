package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.item.DyeableCondensedSpecterItem;
import com.cursee.specter.impl.common.item.SpecterCoreItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;

public class ModItems {

  public static Item SPECTER_CORE;
  public static Item CONDENSED_SPECTER;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {
    SPECTER_CORE = new SpecterCoreItem(new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
    CONDENSED_SPECTER = new DyeableCondensedSpecterItem(new Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));

    consumer.accept(SPECTER_CORE, SpecterCommon.identifier("specter_core"));
    consumer.accept(CONDENSED_SPECTER, SpecterCommon.identifier("condensed_specter"));
  }
}
