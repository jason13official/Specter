package com.cursee.specter;

import com.cursee.specter.impl.common.registry.ModItems;
import com.cursee.specter.impl.fabric.common.loot.FabricLootModifiers;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class SpecterFabric implements ModInitializer {

  public static <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  @Override
  public void onInitialize() {

    // bind before common init
    bind(BuiltInRegistries.ITEM, ModItems::register);

    SpecterCommon.init();

    // before integrated/dedicated server launch (attributes)

    ServerLifecycleEvents.SERVER_STARTING.register(SpecterServer::onServerStarting);
    ServerLifecycleEvents.SERVER_STARTED.register(SpecterServer::onServerStarted);

    // server running events
    ServerEntityEvents.ENTITY_LOAD.register(SpecterServer::onEntityJoinServerLevel);

    ServerLifecycleEvents.SERVER_STOPPING.register(SpecterServer::onServerStopping);
    ServerLifecycleEvents.SERVER_STOPPED.register(SpecterServer::onServerStopped);

    // loader specific
    FabricLootModifiers.register();
  }
}
