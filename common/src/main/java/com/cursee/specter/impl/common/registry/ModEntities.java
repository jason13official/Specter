package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.entity.Specter;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static ResourceLocation SPECTER_ID;
  public static EntityType<Specter> SPECTER;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {

    SPECTER_ID = SpecterCommon.identifier("specter");
    SPECTER = EntityType.Builder.<Specter>of(Specter::new, MobCategory.AMBIENT).sized(0.0625F * 6f, 0.0625F * 6f).clientTrackingRange(8).build(SPECTER_ID.toString());
    consumer.accept(SPECTER, SPECTER_ID);
  }
}
