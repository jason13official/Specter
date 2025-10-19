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

  @Override
  public void tick() {
    super.tick();

    // new logic
  }
}
