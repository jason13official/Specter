package com.cursee.specter;

import com.cursee.specter.platform.Services;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class SpecterServer {

  public static void onServerStarting(final MinecraftServer server) {
  }

  public static void onServerStarted(final MinecraftServer server) {
  }

  public static void onEntityJoinServerLevel(Entity abstractEntity, ServerLevel serverLevel) {
    if (Services.PLATFORM.isDevelopmentEnvironment() && abstractEntity instanceof ServerPlayer serverPlayer) {
      Constants.LOG.info("ServerPlayer joined world with name and UUID {} {} ", serverPlayer.getName().getString(), serverPlayer.getStringUUID());
    }
  }

  public static void onServerStopping(final MinecraftServer server) {
  }

  public static void onServerStopped(final MinecraftServer server) {
  }
}
