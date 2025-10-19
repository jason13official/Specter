package com.cursee.specter;

import com.cursee.specter.impl.common.registry.ModEntities;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class SpecterForge {

  public static IEventBus eventBus;

  public SpecterForge(final FMLJavaModLoadingContext context) {

    SpecterForge.eventBus = context.getModEventBus();

    bind(Registries.ENTITY_TYPE, ModEntities::register);

    SpecterCommon.init();

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new SpecterClientForge();
    }

    MinecraftForge.EVENT_BUS.addListener((Consumer<ServerStartingEvent>) event -> SpecterServer.onServerStarting(event.getServer()));
    MinecraftForge.EVENT_BUS.addListener((Consumer<ServerStartedEvent>) event -> SpecterServer.onServerStarted(event.getServer()));
    MinecraftForge.EVENT_BUS.addListener((Consumer<ServerStoppingEvent>) event -> SpecterServer.onServerStopping(event.getServer()));
    MinecraftForge.EVENT_BUS.addListener((Consumer<ServerStoppedEvent>) event -> SpecterServer.onServerStopped(event.getServer()));
  }

  @Deprecated(forRemoval = true, since = "1.21.1")
  @SuppressWarnings("all")
  public SpecterForge() {
    this(FMLJavaModLoadingContext.get());
    Constants.LOG.info("It is recommended that you update to the latest version of Forge for 1.20.1");
    Constants.LOG.info("See https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html for new versions.");
  }

  public static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
    SpecterForge.eventBus.addListener((Consumer<RegisterEvent>) event -> {
      if (registry.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registry, rl, () -> t));
      }
    });
  }
}