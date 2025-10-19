package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.entity.AbstractRawSpecter;
import com.cursee.specter.impl.common.entity.RawSpecter;
import com.cursee.specter.impl.common.entity.Specter;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static final ResourceLocation SPECTER_ID = SpecterCommon.identifier("specter");
  public static final ResourceLocation RAW_SPECTER_ID = SpecterCommon.identifier("raw_specter");
  public static EntityType<Specter> SPECTER;
  public static EntityType<RawSpecter> RAW_SPECTER;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {
    SPECTER = EntityType.Builder.<Specter>of(Specter::new, MobCategory.AMBIENT).sized(0.9F, 0.5F).clientTrackingRange(8).build(SPECTER_ID.toString());
    RAW_SPECTER = EntityType.Builder.<RawSpecter>of(RawSpecter::new, MobCategory.AMBIENT).sized(0.0625F * 2f, 0.0625F * 2f).clientTrackingRange(8).build(RAW_SPECTER_ID.toString());
    consumer.accept(SPECTER, SPECTER_ID);
    consumer.accept(RAW_SPECTER, RAW_SPECTER_ID);
  }
}
