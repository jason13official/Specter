package com.cursee.specter.api.common.accessor;

import com.cursee.specter.mixin.ServerPlayerPersistentDataMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/// Automatically implemented on {@link ServerPlayer} by {@link ServerPlayerPersistentDataMixin}
public interface PersistentDataHolder {

  CompoundTag specter$getPersistentData();

  void specter$setPersistentData(CompoundTag compoundTag);
}
