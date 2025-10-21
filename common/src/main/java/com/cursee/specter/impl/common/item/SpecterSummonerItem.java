package com.cursee.specter.impl.common.item;

import com.cursee.specter.impl.common.entity.Specter;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SpecterSummonerItem extends SpecterCoreItem {

  private DyeColor dyeColor = DyeColor.WHITE;

  public SpecterSummonerItem(Properties properties, DyeColor dyeColor) {
    super(properties.rarity(Rarity.EPIC));
    this.dyeColor = dyeColor;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

    AtomicBoolean foundSpecterOwnedBySelf = new AtomicBoolean();
    level.getNearbyEntities(Specter.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(64.0D)).forEach(rawSpecter -> {
      if (rawSpecter.getOwner() == player) {
        foundSpecterOwnedBySelf.set(true);
      }
    });

    if (!foundSpecterOwnedBySelf.get()) {
      if (!player.level().isClientSide()) {

        DyeColor spawnColor = this.dyeColor; // DyeColor.byId(player.getRandom().nextInt(0, 15));

        var specter = new Specter(player.level(), player, spawnColor);

        if (player.getItemInHand(usedHand).hasCustomHoverName()) {
          specter.setCustomName(player.getItemInHand(usedHand).getHoverName());
        }

        specter.moveTo(player.position());
        player.level().addFreshEntity(specter);
      }
      player.setItemInHand(usedHand, ItemStack.EMPTY);
    }

    return super.use(level, player, usedHand);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
