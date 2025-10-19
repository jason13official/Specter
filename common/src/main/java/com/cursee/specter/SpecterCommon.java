package com.cursee.specter;

import net.minecraft.resources.ResourceLocation;

public class SpecterCommon {

  public static void init() {
  }

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.tryBuild(Constants.MOD_ID, path);
  }
}