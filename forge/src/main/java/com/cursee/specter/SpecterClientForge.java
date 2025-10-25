package com.cursee.specter;

import com.cursee.specter.impl.client.model.SpecterModel;
import com.cursee.specter.impl.client.renderer.entity.SpecterRenderer;
import com.cursee.specter.impl.common.registry.ModEntities;
import com.cursee.specter.impl.common.registry.ModItems;
import java.util.function.Consumer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class SpecterClientForge {

  public SpecterClientForge() {

    SpecterClient.init();

    SpecterForge.eventBus.addListener((Consumer<RegisterColorHandlersEvent.Item>) event -> {
      event.register(SpecterClient.DYED_LEATHER_COLOR_FN::apply, ModItems.CONDENSED_SPECTER);
    });

    SpecterForge.eventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
      event.registerLayerDefinition(SpecterRenderer.LAYER_LOCATION, SpecterModel::createBodyLayer);
    });

    SpecterForge.eventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
      event.registerEntityRenderer(ModEntities.SPECTER, SpecterRenderer::new);
    });
  }
}
