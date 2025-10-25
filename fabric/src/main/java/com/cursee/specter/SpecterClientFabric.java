package com.cursee.specter;

import com.cursee.specter.impl.client.model.SpecterModel;
import com.cursee.specter.impl.client.renderer.entity.SpecterRenderer;
import com.cursee.specter.impl.common.registry.ModEntities;
import com.cursee.specter.impl.common.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SpecterClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SpecterClient.init();

    ColorProviderRegistry.ITEM.register(SpecterClient.DYED_LEATHER_COLOR_FN::apply, ModItems.CONDENSED_SPECTER);

    EntityModelLayerRegistry.registerModelLayer(SpecterRenderer.LAYER_LOCATION, SpecterModel::createBodyLayer);

    EntityRendererRegistry.register(ModEntities.SPECTER, SpecterRenderer::new);
  }
}
