package com.cursee.specter.impl.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBoringEntity extends Mob {

  public AbstractBoringEntity(EntityType<? extends AbstractBoringEntity> entityType, Level level) {
    super(entityType, level);
  }

  @Override
  public boolean shouldShowName() {
    return this.hasCustomName();
  }

  @Override
  public boolean isPickable() {
    return true;
  }

  @Override
  public boolean canBeCollidedWith() {
    return false;
  }

  @Override
  public boolean canCollideWith(Entity entity) {
    return entity instanceof AbstractSpecter;
  }

  @Override
  public boolean shouldRender(double x, double y, double z) {
    return true;
  }

  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    return true;
  }

  @Override
  public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
    return false;
  }

  @Override
  protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
    return 0;
  }

  @Override
  protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    // no-op
  }
}
