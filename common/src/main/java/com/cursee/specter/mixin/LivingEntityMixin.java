package com.cursee.specter.mixin;

import com.cursee.specter.api.common.util.SpecterHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Prevent player death if they have a specter nearby
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void specter$checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {

    LivingEntity self = (LivingEntity) (Object) this;

    if (self instanceof Player player && SpecterHelper.hasOwnedSpecterNearby(player)) {
      self.setHealth(2.0f);
      cir.setReturnValue(true);
    }
  }
}
