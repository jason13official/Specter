package com.cursee.specter.impl.common.entity;

import com.cursee.specter.Constants;
import com.cursee.specter.platform.Services;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSpecter extends AbstractBoringEntity implements TraceableEntity {

  public static final String SPECTER_OWNER_TAG = "specter_owner";
  public static final String SPECTER_COLOR_TAG = "specter_color";
  public static final EntityDataAccessor<Optional<UUID>> OPTIONAL_OWNER_UUID = SynchedEntityData.defineId(AbstractSpecter.class, EntityDataSerializers.OPTIONAL_UUID);
  public static final EntityDataAccessor<Integer> SPECTER_COLOR = SynchedEntityData.defineId(AbstractSpecter.class, EntityDataSerializers.INT);

  private @Nullable LivingEntity owner;

  protected AbstractSpecter(EntityType<? extends AbstractSpecter> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  @Override
  public @Nullable LivingEntity getOwner() {
    return owner;
  }

  public void setOwner(@Nullable LivingEntity newOwner) {
    if (newOwner != null && !newOwner.isDeadOrDying()) {
      this.owner = newOwner;
      this.setOwnerId(newOwner.getUUID());
    }
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.getEntityData().define(OPTIONAL_OWNER_UUID, Optional.empty());
    this.getEntityData().define(SPECTER_COLOR, DyeColor.WHITE.getId());
  }

  public @NotNull Optional<UUID> getOwnerId() {
    return this.getEntityData().get(OPTIONAL_OWNER_UUID);
  }

  public void setOwnerId(@NotNull UUID uuid) {
    this.entityData.set(OPTIONAL_OWNER_UUID, Optional.of(uuid));
  }

  public int getSpecterColor() {
    return this.getEntityData().get(SPECTER_COLOR);
  }

  public float[] getDiffuseSpecterColors() {

    int textureDefuseColor = this.getSpecterColor();

    int i = (textureDefuseColor & 16711680) >> 16;
    int j = (textureDefuseColor & '\uff00') >> 8;
    int k = (textureDefuseColor & 255) >> 0;

    return new float[]{(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
  }

  public void setSpecterColor(int color) {
    this.entityData.set(SPECTER_COLOR, color);
  }

  @Override
  public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);

    if (compoundTag.contains(SPECTER_OWNER_TAG, Tag.TAG_INT_ARRAY) && compoundTag.getIntArray(SPECTER_OWNER_TAG).length == 4) {
      this.setOwnerId(compoundTag.getUUID(SPECTER_OWNER_TAG));
    }

    if (compoundTag.contains(SPECTER_COLOR_TAG, Tag.TAG_INT)) {
      this.setSpecterColor(compoundTag.getInt(SPECTER_COLOR_TAG));
    }
  }

  @Override
  public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);

    this.getOwnerId().ifPresent(uuid -> {
      compoundTag.putUUID(SPECTER_OWNER_TAG, uuid);
    });

    compoundTag.putInt(SPECTER_COLOR_TAG, this.getSpecterColor());
  }

  public void attemptResynchronization() {

    // fix possible desync after player respawns
    boolean ownerIdPresent = this.getOwnerId().isPresent();
    boolean ownerIsPlayer = this.owner instanceof Player;
    if (ownerIdPresent && ownerIsPlayer && this.level() instanceof ServerLevel serverLevel && this.owner != serverLevel.getPlayerByUUID(this.getOwnerId().get())) {
      if (Services.PLATFORM.isDevelopmentEnvironment()) Constants.LOG.info("Owner is instance of a player, but owner is not using correct object.");
      this.setOwner(serverLevel.getPlayerByUUID(this.getOwnerId().get()));
    }

    // find owner by synced UUID
    if (this.getOwnerId().isPresent() && this.getOwner() == null) {
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("Owner ID was present, but actual owner object was null. Client side? {}", this.level().isClientSide());
      }

      UUID ownerId = this.getOwnerId().get();
      Player playerByOwnerId = this.level().getPlayerByUUID(ownerId);
      if (playerByOwnerId != null) {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
          Constants.LOG.info("Discovered player owner by UUID, setting as owner.");
        }
        this.setOwner(playerByOwnerId);
      }

      var entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(64, 8, 64));

      for (int i = 0; i < entities.size(); i++) {
        LivingEntity living = entities.get(i);

        if (living.getUUID().equals(ownerId)) {
          if (Services.PLATFORM.isDevelopmentEnvironment()) {
            Constants.LOG.info("Discovered living owner by UUID, setting as owner.");
          }
          this.setOwner(living);
        }
      }
    }
  }

  private void handleLookingAtOwner() {

    float maxRotDegrees = 8.0f;

    if (this.getOwner() != null) {

      Vec3 targetPositionDelta = new Vec3(this.getOwner().getX() - this.getX(), this.getOwner().getY() + (double) this.getOwner().getEyeHeight() + 0.25f - this.getY(),
          this.getOwner().getZ() - this.getZ());

      double targetPosDeltaSquared = targetPositionDelta.lengthSqr();

      if (targetPosDeltaSquared > (double) 16.0f) {
        maxRotDegrees = 16.0f;
      }

      this.lookAt(this.getOwner(), maxRotDegrees, maxRotDegrees);
    }
  }

  @Override
  public void tick() {

    super.tick();

    this.xo = this.getX();
    this.yo = this.getY();
    this.zo = this.getZ();

    this.handleLookingAtOwner();

    if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
      this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) * 0.2F, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }

    if (!this.level().noCollision(this.getBoundingBox())) {
      this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / (double) 2.0F, this.getZ());
    }

    if (this.tickCount % 20 == 1) {
      this.attemptResynchronization();
    }

    if (this.getOwner() != null && (this.getOwner().isSpectator() || this.getOwner().isDeadOrDying())) {
      this.setOwner(null);
    }

    if (this.getOwner() != null) {
      // we are targeting slightly above the player's eye height
      Vec3 targetPositionDelta = new Vec3(this.getOwner().getX() - this.getX(), this.getOwner().getY() + (double) this.getOwner().getEyeHeight() + 0.25f - this.getY(),
          this.getOwner().getZ() - this.getZ());

      double targetPosDeltaSquared = targetPositionDelta.lengthSqr();

      double movementDampening = 1.0f;

      // if player is closer than ~3 blocks, dampen movement
      if (targetPosDeltaSquared < 8.0f) {
        movementDampening = 0.5f;
      }

      // if the player exists in the same level and is greater than 2 blocks (2 * 2 = 4)
      if (this.level().dimension().equals(this.getOwner().level().dimension()) && targetPosDeltaSquared > 4) {
        this.setDeltaMovement(this.getDeltaMovement().add(targetPositionDelta.normalize().scale(movementDampening * movementDampening * 0.1)));
      }
    } else {
      double dampening = 0.5f;
      RandomSource random = this.level().getRandom();
      Vec3 movement = this.getDeltaMovement().add((random.nextFloat() * 2f) - 1f, (random.nextFloat() * 2f) - 1f, (random.nextFloat() * 2f) - 1f).scale(dampening * dampening * 0.1);
      this.setDeltaMovement(movement);
    }

    this.move(MoverType.SELF, this.getDeltaMovement());

    // base friction factor for slowing in air or water
    float friction = 0.49F; // ?????

    // use friction provided by the block and normalize relative decay
    if (this.onGround()) {
      friction = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
    }

    // apply friction to x-axis and z-axis, slightly dampen y-axis (reduce velocity over time)
    this.setDeltaMovement(this.getDeltaMovement().multiply(friction, 0.98, friction));

    // bouncy bouncy bouncy
    if (this.onGround()) {
      this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, -0.9, 1.0F));
    }
  }
}
