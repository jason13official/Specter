package com.cursee.specter.impl.client.renderer.entity;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.client.model.SpecterModel;
import com.cursee.specter.impl.common.entity.Specter;
import com.cursee.specter.impl.common.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpecterRenderer extends MobRenderer<Specter, SpecterModel<Specter>> {

  private static final ResourceLocation TEXTURE_LOCATION = SpecterCommon.identifier("textures/entity/specter.png");
  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModEntities.SPECTER_ID, "main");

  public SpecterRenderer(Context context) {
    super(context, new SpecterModel<Specter>(context.bakeLayer(LAYER_LOCATION)), 0.75f);
  }

  @Override
  public ResourceLocation getTextureLocation(Specter specter) {
    return TEXTURE_LOCATION;
  }
}
