package com.cursee.specter.impl.common.entity;

import com.cursee.specter.Constants;
import com.cursee.specter.impl.common.registry.ModEntities;
import com.cursee.specter.platform.Services;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RawSpecter extends AbstractRawSpecter {

  public RawSpecter(EntityType<? extends AbstractRawSpecter> entityType, Level level) {
    super(entityType, level);
  }

  public RawSpecter(Level level, @Nullable LivingEntity owner) {
    this(ModEntities.RAW_SPECTER, level);
    this.setOwner(owner);
  }

  @Override
  public boolean isPickable() {
    return true;
  }

//  @Override
//  public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
//
//    // if player interacting with unowned specter
//    // create new raw specter owned by the player and move to player location
//    if (this.getOwner() == null) {
//      var specter = new RawSpecter(this.level(), player);
//      specter.moveTo(player.position());
//      this.level().addFreshEntity(specter);
//    }
//
//    return super.interact(player, hand);
//  }


  @Override
  protected InteractionResult mobInteract(Player player, InteractionHand hand) {

    // if player interacting with unowned specter
    // create new raw specter owned by the player and move to player location
    if (!this.level().isClientSide() && hand == InteractionHand.MAIN_HAND && this.getOwner() == null) {
      var specter = new RawSpecter(this.level(), player);
      specter.moveTo(player.position());
      this.level().addFreshEntity(specter);
    }

    return super.mobInteract(player, hand);
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
