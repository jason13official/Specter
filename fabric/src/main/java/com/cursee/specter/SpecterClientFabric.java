package com.cursee.specter;

import com.cursee.specter.impl.common.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class SpecterClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SpecterClient.init();

    ColorProviderRegistry.ITEM.register(SpecterClient.DYED_LEATHER_COLOR_FN::apply, ModItems.CONDENSED_SPECTER);
  }
}
