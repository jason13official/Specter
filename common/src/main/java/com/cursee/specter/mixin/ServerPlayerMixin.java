package com.cursee.specter.mixin;

import com.cursee.specter.api.server.specter.ISpecterPlayer;
import com.cursee.specter.impl.common.entity.Specter;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements ISpecterPlayer {

  @Unique
  private Specter specter$specter = null;

  @Unique
  @Override
  public Specter specter$getSpecter() {
    return this.specter$specter;
  }

  @Unique
  @Override
  public void specter$setSpecter(Specter specter) {
    this.specter$specter = specter;
  }
}
