package com.cursee.specter;

import com.cursee.specter.impl.common.entity.Specter;
import com.cursee.specter.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;

public class SpecterServer {

  public static void onServerStarting(final MinecraftServer server) {
  }

  public static void onServerStarted(final MinecraftServer server) {
  }

  public static void onEntityJoinServerLevel(Entity abstractEntity, ServerLevel serverLevel) {

    if (abstractEntity instanceof Player && Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("server game time {} day time {}", serverLevel.getGameTime(), serverLevel.getDayTime());

      Constants.LOG.info("value of calculation for querying days in TimeCommand {}", serverLevel.getDayTime() / 24000L % 2147483647L);
    }


    if (serverLevel.getDayTime() / 24000L % 2147483647L > 10 && abstractEntity instanceof Monster monster) {
      System.out.println("Entity joined server after day 10?");

      int spawnColor = DyeColor.byId(serverLevel.getRandom().nextInt(0, 16)).getTextColor();

      var specter = new Specter(serverLevel, monster, spawnColor);

//      if (player.getItemInHand(usedHand).hasCustomHoverName()) {
//        specter.setCustomName(player.getItemInHand(usedHand).getHoverName());
//      }

      // generate a random name?

      specter.setCustomName(Component.literal(String.valueOf(serverLevel.getRandom().nextInt(111111, 1000000)))); // 111,111 to 999,999

      specter.moveTo(monster.position());
      serverLevel.addFreshEntity(specter);
    }

  }

  public static void onServerStopping(final MinecraftServer server) {
  }

  public static void onServerStopped(final MinecraftServer server) {
  }
}
