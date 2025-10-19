package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.item.CreateSpecterItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

  public static Item CREATE_SPECTER_ITEM;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    CREATE_SPECTER_ITEM = new CreateSpecterItem(new Item.Properties().stacksTo(1));

    consumer.accept(CREATE_SPECTER_ITEM, SpecterCommon.identifier("create_specter_item"));
  }
}
