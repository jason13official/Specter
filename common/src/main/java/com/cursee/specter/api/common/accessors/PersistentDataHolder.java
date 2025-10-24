package com.cursee.specter.api.common.accessors;

import net.minecraft.nbt.CompoundTag;

public interface PersistentDataHolder {

  CompoundTag specter$getPersistentData();

  void specter$setPersistentData(CompoundTag compoundTag);

}
