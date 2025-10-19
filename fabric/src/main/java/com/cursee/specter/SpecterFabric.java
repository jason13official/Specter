package com.cursee.specter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class SpecterFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    SpecterCommon.init();

    ServerLifecycleEvents.SERVER_STARTING.register(SpecterServer::onServerStarting);
    ServerLifecycleEvents.SERVER_STARTED.register(SpecterServer::onServerStarted);
    ServerLifecycleEvents.SERVER_STOPPING.register(SpecterServer::onServerStopping);
    ServerLifecycleEvents.SERVER_STOPPED.register(SpecterServer::onServerStopped);
  }
}
