package com.cursee.specter.mixin;

import com.cursee.specter.impl.common.item.AllayThrottlerItem;
import com.cursee.specter.impl.common.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public class AllayMixin {

  @Inject(at = @At("HEAD"), method = "mobInteract")
  private void specter$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    if (player.getItemInHand(hand).getItem() instanceof AllayThrottlerItem) {

      Allay allay = (Allay) (Object) this;

      Vec3 pos = allay.position();

      float tinyRandomOffset = ((player.getRandom().nextFloat() * 2) - 1) * 0.25f;

      for (int i = 0; i < 4; i++) {
        player.level().addParticle(ParticleTypes.SMOKE, pos.x + tinyRandomOffset, pos.y + tinyRandomOffset, pos.z + tinyRandomOffset, 0, 0, 0);
      }

      allay.discard();

      ItemStack stack = new ItemStack(ModItems.THROTTLED_ALLAY_TOTEM);
      // player.addItem(stack);

      player.setItemInHand(hand, stack);
    }
  }
}
