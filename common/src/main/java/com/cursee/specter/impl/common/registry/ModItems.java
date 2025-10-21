package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.item.SpecterCoreItem;
import com.cursee.specter.impl.common.item.SpecterSummonerItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

  public static Item SPECTER_CORE;

  public static Item SPECTER_SUMMONER_WHITE;
  public static Item SPECTER_SUMMONER_ORANGE;
  public static Item SPECTER_SUMMONER_MAGENTA;
  public static Item SPECTER_SUMMONER_LIGHT_BLUE;
  public static Item SPECTER_SUMMONER_YELLOW;
  public static Item SPECTER_SUMMONER_LIME;
  public static Item SPECTER_SUMMONER_PINK;
  public static Item SPECTER_SUMMONER_GRAY;
  public static Item SPECTER_SUMMONER_LIGHT_GRAY;
  public static Item SPECTER_SUMMONER_CYAN;
  public static Item SPECTER_SUMMONER_PURPLE;
  public static Item SPECTER_SUMMONER_BLUE;
  public static Item SPECTER_SUMMONER_BROWN;
  public static Item SPECTER_SUMMONER_GREEN;
  public static Item SPECTER_SUMMONER_RED;
  public static Item SPECTER_SUMMONER_BLACK;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    SPECTER_CORE = new SpecterCoreItem(new Item.Properties());

    SPECTER_SUMMONER_WHITE = new SpecterSummonerItem(new Item.Properties(), DyeColor.WHITE);
    SPECTER_SUMMONER_ORANGE = new SpecterSummonerItem(new Item.Properties(), DyeColor.ORANGE);
    SPECTER_SUMMONER_MAGENTA = new SpecterSummonerItem(new Item.Properties(), DyeColor.MAGENTA);
    SPECTER_SUMMONER_LIGHT_BLUE = new SpecterSummonerItem(new Item.Properties(), DyeColor.LIGHT_BLUE);
    SPECTER_SUMMONER_YELLOW = new SpecterSummonerItem(new Item.Properties(), DyeColor.YELLOW);
    SPECTER_SUMMONER_LIME = new SpecterSummonerItem(new Item.Properties(), DyeColor.LIME);
    SPECTER_SUMMONER_PINK = new SpecterSummonerItem(new Item.Properties(), DyeColor.PINK);
    SPECTER_SUMMONER_GRAY = new SpecterSummonerItem(new Item.Properties(), DyeColor.GRAY);
    SPECTER_SUMMONER_LIGHT_GRAY = new SpecterSummonerItem(new Item.Properties(), DyeColor.LIGHT_GRAY);
    SPECTER_SUMMONER_CYAN = new SpecterSummonerItem(new Item.Properties(), DyeColor.CYAN);
    SPECTER_SUMMONER_PURPLE = new SpecterSummonerItem(new Item.Properties(), DyeColor.PURPLE);
    SPECTER_SUMMONER_BLUE = new SpecterSummonerItem(new Item.Properties(), DyeColor.BLUE);
    SPECTER_SUMMONER_BROWN = new SpecterSummonerItem(new Item.Properties(), DyeColor.BROWN);
    SPECTER_SUMMONER_GREEN = new SpecterSummonerItem(new Item.Properties(), DyeColor.GREEN);
    SPECTER_SUMMONER_RED = new SpecterSummonerItem(new Item.Properties(), DyeColor.RED);
    SPECTER_SUMMONER_BLACK = new SpecterSummonerItem(new Item.Properties(), DyeColor.BLACK);

    consumer.accept(SPECTER_CORE, SpecterCommon.identifier("specter_core"));
    consumer.accept(SPECTER_SUMMONER_WHITE, SpecterCommon.identifier("specter_summoner_white"));
    consumer.accept(SPECTER_SUMMONER_ORANGE, SpecterCommon.identifier("specter_summoner_orange"));
    consumer.accept(SPECTER_SUMMONER_MAGENTA, SpecterCommon.identifier("specter_summoner_magenta"));
    consumer.accept(SPECTER_SUMMONER_LIGHT_BLUE, SpecterCommon.identifier("specter_summoner_light_blue"));
    consumer.accept(SPECTER_SUMMONER_YELLOW, SpecterCommon.identifier("specter_summoner_yellow"));
    consumer.accept(SPECTER_SUMMONER_LIME, SpecterCommon.identifier("specter_summoner_lime"));
    consumer.accept(SPECTER_SUMMONER_PINK, SpecterCommon.identifier("specter_summoner_pink"));
    consumer.accept(SPECTER_SUMMONER_GRAY, SpecterCommon.identifier("specter_summoner_gray"));
    consumer.accept(SPECTER_SUMMONER_LIGHT_GRAY, SpecterCommon.identifier("specter_summoner_light_gray"));
    consumer.accept(SPECTER_SUMMONER_CYAN, SpecterCommon.identifier("specter_summoner_cyan"));
    consumer.accept(SPECTER_SUMMONER_PURPLE, SpecterCommon.identifier("specter_summoner_purple"));
    consumer.accept(SPECTER_SUMMONER_BLUE, SpecterCommon.identifier("specter_summoner_blue"));
    consumer.accept(SPECTER_SUMMONER_BROWN, SpecterCommon.identifier("specter_summoner_brown"));
    consumer.accept(SPECTER_SUMMONER_GREEN, SpecterCommon.identifier("specter_summoner_green"));
    consumer.accept(SPECTER_SUMMONER_RED, SpecterCommon.identifier("specter_summoner_red"));
    consumer.accept(SPECTER_SUMMONER_BLACK, SpecterCommon.identifier("specter_summoner_black"));
  }
}
