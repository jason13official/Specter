package com.cursee.specter.impl.common.entity;

import com.cursee.specter.impl.common.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;

/// @see Phantom
public class Specter extends FlyingMob {

  /// This constructor is used for {@link EntityType} registration in {@link ModEntities}
  public Specter(EntityType<? extends FlyingMob> entityType, Level level) {
    super(entityType, level);
  }

  public Specter(Level level) {
    this(ModEntities.SPECTER, level);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes();
  }
}
