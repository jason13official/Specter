package com.cursee.specter.impl.common.entity;

import com.cursee.specter.Constants;
import com.cursee.specter.platform.Services;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSpecter extends Mob implements TraceableEntity {

  public static final String SPECTER_OWNER_TAG = "specter_owner";
  public static final String SPECTER_COLOR_TAG = "specter_color";
  public static final EntityDataAccessor<Optional<UUID>> OPTIONAL_OWNER_UUID = SynchedEntityData.defineId(AbstractSpecter.class, EntityDataSerializers.OPTIONAL_UUID);
  public static final EntityDataAccessor<Integer> DYE_COLOR_ID = SynchedEntityData.defineId(AbstractSpecter.class, EntityDataSerializers.INT);

  private @Nullable LivingEntity owner;
  private DyeColor dyeColor = DyeColor.WHITE;

  public AbstractSpecter(EntityType<? extends AbstractSpecter> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  @Override
  public boolean shouldShowName() {
    return this.hasCustomName();
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.getEntityData().define(OPTIONAL_OWNER_UUID, Optional.empty());
    this.getEntityData().define(DYE_COLOR_ID, DyeColor.WHITE.getId());
  }

  public @NotNull Optional<UUID> getOwnerId() {
    return this.getEntityData().get(OPTIONAL_OWNER_UUID);
  }

  public void setOwnerId(@NotNull UUID uuid) {
    this.entityData.set(OPTIONAL_OWNER_UUID, Optional.of(uuid));
  }

  public DyeColor getDyeColor() {
    return DyeColor.byId(this.getEntityData().get(DYE_COLOR_ID));
  }

  public void setDyeColor(DyeColor dyeColor) {
    this.entityData.set(DYE_COLOR_ID, dyeColor.getId());
  }

  @Override
  public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);

    if (compoundTag.contains(SPECTER_OWNER_TAG, Tag.TAG_INT_ARRAY) && compoundTag.getIntArray(SPECTER_OWNER_TAG).length == 4) {
      this.setOwnerId(compoundTag.getUUID(SPECTER_OWNER_TAG));
    }

    if (compoundTag.contains(SPECTER_COLOR_TAG, Tag.TAG_INT)) {
      this.setDyeColor(DyeColor.byId(compoundTag.getInt(SPECTER_COLOR_TAG) % 16));
    }
  }

  @Override
  public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);

    this.getOwnerId().ifPresent(uuid -> {
      compoundTag.putUUID(SPECTER_OWNER_TAG, uuid);
    });

    compoundTag.putInt(SPECTER_COLOR_TAG, this.getDyeColor().getId());
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
  public @Nullable LivingEntity getOwner() {
    return this.owner;
  }

  public void setOwner(@Nullable LivingEntity newOwner) {
    if (newOwner != null && !newOwner.isDeadOrDying()) {
      this.owner = newOwner;
      this.setOwnerId(newOwner.getUUID());
    }
  }

  public void scanForEntities() {


    // fix server desync
    // if the owner is not null, but the player is not the same, map to actual player
    // owned by  a player, but underlying LivingEntity owner is not the same object
    if (this.owner != null && this.getOwnerId().isPresent() && this.level() instanceof ServerLevel serverLevel) {

      UUID ownerId = this.getOwnerId().get();

      for (Entity entity : serverLevel.getAllEntities()) {
        if (entity instanceof ServerPlayer serverPlayer && ownerId.equals(serverPlayer.getUUID())) {
          this.setOwner(serverPlayer);
        }
      }
    }

    // if (this.owner == null || this.owner.distanceToSqr(this) > (double)4.0f) {
    if (this.owner == null) {

      if (Services.PLATFORM.isDevelopmentEnvironment() && this.level().isClientSide()) {
        Constants.LOG.info("Missing owner on client, searching...");
      }

      // ExperienceOrb sets followingPlayer to the nearest available player
      // this.owner = this.level().getNearestPlayer(this, (double)8.0F);

      // we are checking against every alive player's UUID
      this.getOwnerId().ifPresent(uuid -> {

        if (Services.PLATFORM.isDevelopmentEnvironment() && this.level().isClientSide()) {
          Constants.LOG.info("Owner UUID {} present on client, still searching...", uuid.toString());
        }

        this.level().players().forEach(player -> {

          UUID playerId = player.getUUID();

          if (uuid.equals(playerId) && !(player.isSpectator() || player.isDeadOrDying())) {

            if (Services.PLATFORM.isDevelopmentEnvironment() && this.level().isClientSide()) {
              Constants.LOG.info("Found player with UUID matching synced owner's UUID, {}", uuid);
              Constants.LOG.info("Setting owner on client.");
            }

            this.setOwner(player);
          }
        });
      });
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

  private void handleLookingAtPlayer() {

    float maxRotDegrees = 8.0f;

    if (this.getOwner() != null) {

      Vec3 targetPositionDelta = new Vec3(this.getOwner().getX() - this.getX(), this.getOwner().getY() + (double)this.getOwner().getEyeHeight() + 0.25f - this.getY(), this.getOwner().getZ() - this.getZ());

      double targetPosDeltaSquared = targetPositionDelta.lengthSqr();

      // if player farther than 4 blocks (2*2 = 4), increase max rotation degrees, otherwise lower
      if (targetPosDeltaSquared > (double)16.0f) {

        // player farther than 4 blocks

        maxRotDegrees = 16.0f;
      }
//      else if (targetPosDeltaSquared < (double)8.0f) {
//
//        // player closer than ~3 blocks
//
//        maxRotDegrees = 1.5f;
//      }

      this.lookAt(this.getOwner(), maxRotDegrees, maxRotDegrees);
    }
  }

  @Override
  public void tick() {

    super.tick();

    this.xo = this.getX();
    this.yo = this.getY();
    this.zo = this.getZ();

    this.handleLookingAtPlayer();

    // this was for a raw entity, but LivingEntity handles gravity now, as we extend from Mob
//    // apply water movement dampening or gravity if not in water
//    if (this.isEyeInFluid(FluidTags.WATER)) {
//      this.setUnderwaterMovement();
//    } else if (!this.isNoGravity()) {
//      this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, -0.03, (double)0.0F));
//    }

    if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
      this.setDeltaMovement((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), (double)0.2F, (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
    }

    if (!this.level().noCollision(this.getBoundingBox())) {
      this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / (double)2.0F, this.getZ());
    }

    if (this.tickCount % 20 == 1) {
      this.scanForEntities();
    }

    if (this.getOwner() != null && (this.getOwner().isSpectator() || this.getOwner().isDeadOrDying())) {
      this.setOwner(null);
    }

    if (this.getOwner() != null) {

      // vector pointing from this entity toward its owner's position

      // experience points target center-mass of the player
      // Vec3 targetPositionDelta = new Vec3(this.getOwner().getX() - this.getX(), this.getOwner().getY() + (double)this.getOwner().getEyeHeight() / (double)2.0F - this.getY(), this.getOwner().getZ() - this.getZ());

      // we are targeting slightly above the player's eye height
      Vec3 targetPositionDelta = new Vec3(this.getOwner().getX() - this.getX(), this.getOwner().getY() + (double)this.getOwner().getEyeHeight() + 0.25f - this.getY(), this.getOwner().getZ() - this.getZ());

      double targetPosDeltaSquared = targetPositionDelta.lengthSqr();

      double movementDampening = 1.0f;

      // if player is closer than ~3 blocks, dampen movement
      if (targetPosDeltaSquared < 8.0f) {
        movementDampening = 0.5f;
      }

      // if player within 16 blocks (16*16 = 256)
      // if (targetPosDeltaSquared < (double)256.0F) {

      // if the player exists in the same level and is greater than 2 blocks (2 * 2 = 4)
      if (this.level().dimension().equals(this.getOwner().level().dimension()) && targetPosDeltaSquared > 4) {

        // smooth falloff in pull strength as the entity gets farther from the owner.
        // double movementDampening = (double)1.0F - Math.sqrt(targetPosDeltaSquared) / (double)8.0F; // original

        // double movementDampening = 1.0f; // ??? no dampening

        // double movementDampening = -((double)1.0F - Math.sqrt(targetPosDeltaSquared) / (double)8.0F); // inverse ?? slow as we get closer ?


        this.setDeltaMovement(this.getDeltaMovement().add(targetPositionDelta.normalize().scale(movementDampening * movementDampening * 0.1)));
      }
    }

    this.move(MoverType.SELF, this.getDeltaMovement());

    // base friction factor for slowing in air or water
    // float friction = 0.98F; // original
    float friction = 0.49F; // ?????

    // use friction provided by the block and normalize relative decay
    if (this.onGround()) {
      friction = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
    }

    // apply friction to x-axis and z-axis, slightly dampen y-axis (reduce velocity over time)
    this.setDeltaMovement(this.getDeltaMovement().multiply((double)friction, 0.98, (double)friction));

    // bouncy bouncy bouncy
    if (this.onGround()) {
      this.setDeltaMovement(this.getDeltaMovement().multiply((double)1.0F, -0.9, (double)1.0F));
    }

//    // discard the entity after 5 minutes
//    ++this.age;
//    if (this.age >= 6000) {
//      this.discard();
//    }
  }
}
