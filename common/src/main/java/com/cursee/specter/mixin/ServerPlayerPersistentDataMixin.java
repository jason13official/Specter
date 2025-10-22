package com.cursee.specter.mixin;

import com.cursee.specter.api.common.accessor.PersistentDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Retrieves old persistent data for reconstructing a player after death/dying.
@Mixin(ServerPlayer.class)
public class ServerPlayerPersistentDataMixin {

  @Inject(at = @At("TAIL"), method = "restoreFrom")
  private void specter$restoreFrom(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {

    ServerPlayer self = (ServerPlayer) (Object) this;

    PersistentDataHolder holder = (PersistentDataHolder) self;
    CompoundTag oldPlayerData = holder.specter$getPersistentData();
    holder.specter$setPersistentData(oldPlayerData);
  }
}
