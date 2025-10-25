package com.cursee.specter.impl.forge.common.loot;

import com.cursee.specter.Constants;
import com.cursee.specter.impl.forge.common.loot.modifier.AddItemModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeLootModifiers {

  public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MOD_ID);

  public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
      LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);

  public static void register(IEventBus bus) {
    LOOT_MODIFIER_SERIALIZERS.register(bus);
  }
}
