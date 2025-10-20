package com.cursee.specter.impl.common.entity;

import com.cursee.specter.impl.common.registry.ModEntities;
import java.util.Random;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

    LivingEntity owner = this.getOwner();
    if (owner instanceof Player player) {

      // oncer per second
      if (this.tickCount % 20 == 0) {

        // heal if close to player
        if (this.distanceTo(player) < 4.0f) {
          this.healOwner(player);
        }
      }
    }
  }

  private void healOwner(Player player) {

    if (player.getHealth() < (player.getMaxHealth() * 0.95f)) {

      player.heal(2.0f);
      player.level().playSound(null, this.blockPosition(), SoundEvents.ALLAY_THROW, SoundSource.AMBIENT, 0.8f, 0.8f);

      Vec3 pos = this.position();
      for (int i = 0; i < 4; i++) {
        float random = player.getRandom().nextFloat();
        float offset = (random * 2) - 1;
        player.level().addParticle(ParticleTypes.HAPPY_VILLAGER, pos.x + (offset / 2), pos.y + (offset / 2), pos.z + (offset / 2), offset, offset, offset);
      }
    }
  }
}
