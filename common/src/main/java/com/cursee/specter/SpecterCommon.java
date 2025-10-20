package com.cursee.specter;

import java.util.Random;
import net.minecraft.resources.ResourceLocation;

public class SpecterCommon {

  public static void init() {
  }

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.tryBuild(Constants.MOD_ID, path);
  }

  public static void main(String[] args) {

    Random random = new Random();

    float lowestAchieved = Float.MAX_VALUE;
    float highestAchieved = Float.MIN_VALUE;

    for (int i = 0; i < 1000; i++) {


      float randomFloat = random.nextFloat();

      float calculated = (randomFloat * 2) - 1;

      if (calculated > highestAchieved) {
        highestAchieved = calculated;
      }

      if (calculated < lowestAchieved) {
        lowestAchieved = calculated;
      }

      System.out.println("Calculated Value: " + String.valueOf(calculated));
    }

    System.out.println("Lowest Value Achieved: " + String.valueOf(lowestAchieved));
    System.out.println("Highest Value Achieved: " + String.valueOf(highestAchieved));
  }
}