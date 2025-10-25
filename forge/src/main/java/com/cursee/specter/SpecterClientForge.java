package com.cursee.specter;

import com.cursee.specter.impl.common.registry.ModItems;
import java.util.function.Consumer;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class SpecterClientForge {

  public SpecterClientForge() {

    SpecterClient.init();

    SpecterForge.eventBus.addListener((Consumer<RegisterColorHandlersEvent.Item>) event -> {
      event.register(SpecterClient.DYED_LEATHER_COLOR_FN::apply, ModItems.CONDENSED_SPECTER);
    });
  }
}
