package com.cursee.specter.impl.common.registry;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.common.entity.Specter;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static final ResourceLocation SPECTER_ID = SpecterCommon.identifier("specter");
  public static EntityType<Specter> SPECTER;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {
    SPECTER = EntityType.Builder.<Specter>of(Specter::new, MobCategory.AMBIENT).sized(0.9F, 0.5F).clientTrackingRange(8).build(SPECTER_ID.toString());
    consumer.accept(SPECTER, SPECTER_ID);
  }
}
