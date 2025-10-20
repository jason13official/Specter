package com.cursee.specter.impl.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class AllayThrottlerItem extends Item {

  public AllayThrottlerItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return stack.getRarity() == Rarity.EPIC;
  }

  //  @Override
//  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
//
//    AtomicBoolean foundSpecterOwnedBySelf = new AtomicBoolean();
//    level.getNearbyEntities(RawSpecter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D)).forEach(rawSpecter -> {
//      if (rawSpecter.getOwner() == player) {
//        foundSpecterOwnedBySelf.set(true);
//      }
//    });
//
//    if (!foundSpecterOwnedBySelf.get()) {
//      if (!player.level().isClientSide()) {
//        var specter = new RawSpecter(player.level(), player);
//        specter.moveTo(player.position());
//        player.level().addFreshEntity(specter);
//      }
//
//      player.setItemInHand(usedHand, ItemStack.EMPTY);
//    }
//
//    return super.use(level, player, usedHand);
//  }
}
