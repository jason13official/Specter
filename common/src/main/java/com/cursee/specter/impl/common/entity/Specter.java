package com.cursee.specter.impl.common.entity;

import com.cursee.specter.impl.common.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Specter extends Entity {

  /// This constructor is used for {@link EntityType} registration in {@link ModEntities}
  public Specter(EntityType<?> entityType, Level level) {
    super(entityType, level);
  }

  public Specter(Level level) {
    this(ModEntities.SPECTER, level);
  }

  @Override
  protected void defineSynchedData() {

  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compoundTag) {

  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compoundTag) {

  }
}
