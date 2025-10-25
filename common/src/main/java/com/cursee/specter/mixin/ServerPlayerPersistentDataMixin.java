package com.cursee.specter.mixin;

import com.cursee.specter.api.common.accessors.PersistentDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerPersistentDataMixin {

  @Inject(at = @At("TAIL"), method = "restoreFrom")
  private void specter$restoreFrom(ServerPlayer oldPlayerInstance, boolean keepEverything, CallbackInfo ci) {

    ServerPlayer newPlayerInstance = (ServerPlayer) (Object) this;

    PersistentDataHolder oldDataHolder = (PersistentDataHolder) oldPlayerInstance;
    PersistentDataHolder newDataHolder = (PersistentDataHolder) newPlayerInstance;

    CompoundTag oldPlayerData = oldDataHolder.specter$getPersistentData();
    newDataHolder.specter$setPersistentData(oldPlayerData);
  }
}
