package com.cursee.specter.impl.common.entity;

import com.cursee.specter.api.common.util.SpecterHelper;
import com.cursee.specter.impl.common.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Specter extends AbstractSpecter {

  public Specter(EntityType<? extends Specter> entityType, Level level) {
    super(ModEntities.SPECTER, level);
  }

  public Specter(Level level, @Nullable LivingEntity owner) {
    this(ModEntities.SPECTER, level);
    this.setOwner(owner);
  }

  public Specter(Level level, @Nullable LivingEntity owner, int specterColor) {
    this(level, owner);
    this.setSpecterColor(specterColor);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
  }

  @Override
  protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {

    if (this.getOwner() == player && hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {

      ItemStack stack = SpecterHelper.convertSpecterToCondensedSpecter(this);

      player.setItemInHand(hand, stack);

      this.discard();
    }

    return super.mobInteract(player, hand);
  }

  @Override
  public void tick() {
    super.tick();

    // new logic

    if (this.tickCount % 20 == 0) {
      if (SpecterHelper.hasOwnedSpecterNearby(this, this.getOwner(), true)) {
        this.healOwner();
      }
    }
  }

  private void healOwner() {

    LivingEntity livingEntity = this.getOwner();

    if (livingEntity != null && !livingEntity.isDeadOrDying() && livingEntity.getHealth() < (livingEntity.getMaxHealth() * 0.95f)) {

      livingEntity.heal(2.0f);

      if (!livingEntity.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 0, true, true));
      }

      livingEntity.level().playSound(null, this.blockPosition(), SoundEvents.ALLAY_THROW, SoundSource.AMBIENT, 0.8f, 0.8f);

      Vec3 pos = this.position();
      for (int i = 0; i < 4; i++) {
        float random = livingEntity.getRandom().nextFloat();
        float offset = (random * 2) - 1;
        livingEntity.level().addParticle(ParticleTypes.HAPPY_VILLAGER, pos.x + (offset / 2), pos.y + (offset / 2), pos.z + (offset / 2), offset, offset, offset);
      }
    }
  }
}
