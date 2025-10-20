package com.cursee.specter.mixin;

import com.cursee.specter.impl.common.entity.RawSpecter;
import com.cursee.specter.impl.common.registry.ModItems;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void specter$checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {

    LivingEntity self = (LivingEntity) (Object) this;

    ItemStack itemstack = null;

    for(InteractionHand interactionhand : InteractionHand.values()) {
      ItemStack checkedStack = self.getItemInHand(interactionhand);
      if (checkedStack.is(ModItems.THROTTLED_ALLAY_TOTEM)) {
        itemstack = checkedStack.copy();
        checkedStack.shrink(1);
        break;
      }
    }

    if (itemstack != null) {
      if (self instanceof ServerPlayer serverPlayer) {
        serverPlayer.awardStat(Stats.ITEM_USED.get(ModItems.THROTTLED_ALLAY_TOTEM));
        // CriteriaTriggers.USED_TOTEM.trigger(player, itemstack);
      }

      if (self instanceof Player player) {

        Level level = player.level();

        AtomicBoolean foundSpecterOwnedBySelf = new AtomicBoolean();
        level.getNearbyEntities(RawSpecter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D)).forEach(rawSpecter -> {
          if (rawSpecter.getOwner() == player) {
            foundSpecterOwnedBySelf.set(true);
          }
        });

        if (!foundSpecterOwnedBySelf.get()) {
          var specter = new RawSpecter(level, player);
          specter.moveTo(player.position());
          level.addFreshEntity(specter);
        }
      }

      self.setHealth(2.0F);
//      self.removeAllEffects();
//      self.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
//      self.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
//      self.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

      // self.level().broadcastEntityEvent(self, (byte)35);
      // self.level().broadcastEntityEvent(self, EntityEvent.TALISMAN_ACTIVATE);


    }

    cir.setReturnValue(itemstack != null);

    // continues to normal check
  }
}
