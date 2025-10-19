package com.cursee.specter.api.server.specter;

import com.cursee.specter.impl.common.entity.Specter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SpecterApi {

  public static void createAndAttachSpecterToPlayer(ServerPlayer serverPlayer, ServerLevel serverLevel) {

    ISpecterPlayer specterPlayer = (ISpecterPlayer) serverPlayer;

    if (specterPlayer.specter$getSpecter() == null) {
      Specter specter = new Specter(serverLevel, serverPlayer);
      specter.moveTo(serverPlayer.position());
      serverLevel.addFreshEntity(specter);
      specterPlayer.specter$setSpecter(specter);
    }
  }
}
