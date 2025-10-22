package com.cursee.specter.api.common.accessor;

import com.cursee.specter.mixin.EntityPersistentDataMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

/// Automatically implemented on {@link Entity} by {@link EntityPersistentDataMixin}
public interface PersistentDataHolder {

  CompoundTag specter$getPersistentData();

  void specter$setPersistentData(CompoundTag compoundTag);
}
