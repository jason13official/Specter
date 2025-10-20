package com.cursee.specter;

import com.cursee.specter.impl.client.model.RawSpecterModel;
import com.cursee.specter.impl.client.renderer.entity.RawSpecterRenderer;
import com.cursee.specter.impl.common.registry.ModEntities;
import java.util.function.Consumer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class SpecterClientForge {

  public SpecterClientForge() {

    SpecterClient.init();

    SpecterForge.eventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
      event.registerLayerDefinition(RawSpecterRenderer.LAYER_LOCATION, RawSpecterModel::createBodyLayer);
    });

    SpecterForge.eventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
      event.registerEntityRenderer(ModEntities.RAW_SPECTER, RawSpecterRenderer::new);
    });
  }
}
