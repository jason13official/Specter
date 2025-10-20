package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.entity.RawSpecter;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
  
  public static final ResourceLocation RAW_SPECTER_ID = SpecterCommon.identifier("raw_specter");
  public static EntityType<RawSpecter> RAW_SPECTER;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {

    RAW_SPECTER = EntityType.Builder.<RawSpecter>of(RawSpecter::new, MobCategory.AMBIENT).sized(0.0625F * 6f, 0.0625F * 6f).clientTrackingRange(8).build(RAW_SPECTER_ID.toString());
    consumer.accept(RAW_SPECTER, RAW_SPECTER_ID);
  }
}
