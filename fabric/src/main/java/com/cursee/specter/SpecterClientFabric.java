package com.cursee.specter;

import com.cursee.specter.impl.client.model.RawSpecterModel;
import com.cursee.specter.impl.client.renderer.entity.RawSpecterRenderer;
import com.cursee.specter.impl.common.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SpecterClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    SpecterClient.init();
    
    EntityModelLayerRegistry.registerModelLayer(RawSpecterRenderer.LAYER_LOCATION, RawSpecterModel::createBodyLayer);

    EntityRendererRegistry.register(ModEntities.RAW_SPECTER, RawSpecterRenderer::new);
  }
}
