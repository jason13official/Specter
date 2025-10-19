package com.cursee.specter.impl.common.item;

import com.cursee.specter.impl.common.entity.RawSpecter;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreateSpecterItem extends Item {

  public CreateSpecterItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

    AtomicBoolean foundSpecterOwnedBySelf = new AtomicBoolean();
    level.getNearbyEntities(RawSpecter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D)).forEach(rawSpecter -> {
      if (rawSpecter.getOwner() == player) {
        foundSpecterOwnedBySelf.set(true);
      }
    });

    if (!foundSpecterOwnedBySelf.get()) {
      if (!player.level().isClientSide()) {
        var specter = new RawSpecter(player.level(), player);
        specter.moveTo(player.position());
        player.level().addFreshEntity(specter);
      }

      player.setItemInHand(usedHand, ItemStack.EMPTY);
    }

    return super.use(level, player, usedHand);
  }
}
