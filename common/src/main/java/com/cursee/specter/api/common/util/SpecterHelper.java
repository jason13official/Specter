package com.cursee.specter.api.common.util;

import com.cursee.specter.impl.common.entity.Specter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SpecterHelper {

  /// Check a 65x8x65 area centered on the player for a Specter owned by the player
  public static boolean hasOwnedSpecterNearby(Player player) {

    if (player == null) {
      return false;
    }

    Level level = player.level();
    boolean foundOwnedSpecter = false;
    List<Specter> specters = level.getNearbyEntities(Specter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D, 4D, 64.0D));

    for (Specter specter : specters) {
      if (specter.getOwner() == player) {
        foundOwnedSpecter = true;

        // stop searching if we found a specter owned by the player
        break;
      }
    }

    return foundOwnedSpecter;
  }

  /// @param shieldingPlayer if false, defaults to {@link SpecterHelper#hasOwnedSpecterNearby(Player)}, otherwise checks if a Specter is close enough to shield the player.
  public static boolean hasOwnedSpecterNearby(Specter specter, Player player, boolean shieldingPlayer) {

    if (specter == null || player == null) {
      return false;
    }

    if (!shieldingPlayer) {
      return SpecterHelper.hasOwnedSpecterNearby(player);
    }

    return specter.distanceTo(player) < 4.0f;
  }
}
