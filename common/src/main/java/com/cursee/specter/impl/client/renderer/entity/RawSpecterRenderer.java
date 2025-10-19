package com.cursee.specter.impl.client.renderer.entity;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.client.model.RawSpecterModel;
import com.cursee.specter.impl.common.entity.AbstractRawSpecter;
import com.cursee.specter.impl.common.registry.ModEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RawSpecterRenderer extends EntityRenderer<AbstractRawSpecter> implements RenderLayerParent<AbstractRawSpecter, EntityModel<AbstractRawSpecter>> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModEntities.RAW_SPECTER_ID, "main");
  private static final ResourceLocation TEXTURE_LOCATION = SpecterCommon.identifier("textures/entity/raw_specter.png");
  private final RawSpecterModel model;

  public RawSpecterRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new RawSpecterModel(context.bakeLayer(LAYER_LOCATION));
  }

  @Override
  public @NotNull RawSpecterModel getModel() {
    return this.model;
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(AbstractRawSpecter specter) {
    return TEXTURE_LOCATION;
  }

  @Override
  public boolean shouldRender(AbstractRawSpecter livingEntity, Frustum camera, double camX, double camY, double camZ) {
    return true;
  }

  @Override
  public void render(AbstractRawSpecter specter, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

    poseStack.pushPose();

    // shifting the pose slightly copied from BoatRenderer.class
    poseStack.translate(0.0F, 0.0625f, 0.0F);

    // rotating to face look direction copied from BoatRenderer.class
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));

    // flipping x-axis and y-axis copied from BoatRenderer.class
    poseStack.scale(-1.0F, -1.0F, 1.0F);

    // call to render method defined by our model
    getModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(specter))), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

    poseStack.popPose();

    super.render(specter, entityYaw, partialTick, poseStack, buffer, packedLight);
  }
}
