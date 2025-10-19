package com.cursee.specter.impl.common.entity;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRawSpecter extends Mob implements TraceableEntity {

  public static final String SPECTER_OWNER_TAG = "specter_owner";
  public static final EntityDataAccessor<Optional<UUID>> OPTIONAL_OWNER_UUID = SynchedEntityData.defineId(AbstractRawSpecter.class, EntityDataSerializers.OPTIONAL_UUID);

  private @Nullable LivingEntity owner;

  public AbstractRawSpecter(EntityType<? extends AbstractRawSpecter> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.getEntityData().define(OPTIONAL_OWNER_UUID, Optional.empty());
  }

  public @NotNull Optional<UUID> getOwnerId() {
    return this.getEntityData().get(OPTIONAL_OWNER_UUID);
  }

  public void setOwnerId(@NotNull UUID uuid) {
    this.entityData.set(OPTIONAL_OWNER_UUID, Optional.of(uuid));
  }

  @Override
  public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);
    if (compoundTag.contains(SPECTER_OWNER_TAG, Tag.TAG_INT_ARRAY) && compoundTag.getIntArray(SPECTER_OWNER_TAG).length == 4) {
      this.setOwnerId(compoundTag.getUUID(SPECTER_OWNER_TAG));
    }
  }

  @Override
  public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);
    this.getOwnerId().ifPresent(uuid -> {
      compoundTag.putUUID(SPECTER_OWNER_TAG, uuid);
    });
  }

  @Override
  public @Nullable LivingEntity getOwner() {
    return this.owner;
  }

  public void setOwner(@Nullable LivingEntity newOwner) {
    if (newOwner != null) {
      this.owner = newOwner;
      this.setOwnerId(newOwner.getUUID());
    }
  }

  public void scanForEntities() {
    if (this.owner == null || this.owner.distanceToSqr(this) > (double)4.0f) {

      // ExperienceOrb sets followingPlayer to the nearest available player
      // this.owner = this.level().getNearestPlayer(this, (double)8.0F);

      // we are checking against every alive player's UUID
      this.getOwnerId().ifPresent(uuid -> this.level().players().forEach(player -> {
        if (uuid.equals(player.getUUID()) && !(player.isSpectator() || player.isDeadOrDying())) {
          this.setOwner(player);
        }
      }));
    }
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
