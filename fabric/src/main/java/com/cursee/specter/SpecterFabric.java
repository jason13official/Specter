package com.cursee.specter;

import com.cursee.specter.impl.common.entity.Specter;
import com.cursee.specter.impl.common.registry.ModEntities;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class SpecterFabric implements ModInitializer {

  public static <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  @Override
  public void onInitialize() {

    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);

    SpecterCommon.init();

    FabricDefaultAttributeRegistry.register(ModEntities.SPECTER, Specter.createAttributes());

    ServerLifecycleEvents.SERVER_STARTING.register(SpecterServer::onServerStarting);
    ServerLifecycleEvents.SERVER_STARTED.register(SpecterServer::onServerStarted);
    ServerLifecycleEvents.SERVER_STOPPING.register(SpecterServer::onServerStopping);
    ServerLifecycleEvents.SERVER_STOPPED.register(SpecterServer::onServerStopped);
  }
}
