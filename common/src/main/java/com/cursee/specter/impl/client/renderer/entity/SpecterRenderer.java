package com.cursee.specter.impl.client.renderer.entity;

import com.cursee.specter.SpecterCommon;
import com.cursee.specter.impl.client.model.SpecterModel;
import com.cursee.specter.impl.common.entity.Specter;
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

public class SpecterRenderer extends EntityRenderer<Specter> implements RenderLayerParent<Specter, EntityModel<Specter>> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModEntities.SPECTER_ID, "main");
  private static final ResourceLocation TEXTURE_LOCATION = SpecterCommon.identifier("textures/entity/specter.png");
  private final SpecterModel model;

  public SpecterRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new SpecterModel(context.bakeLayer(LAYER_LOCATION));
  }

  @Override
  public @NotNull SpecterModel getModel() {
    return this.model;
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(Specter specter) {
    return TEXTURE_LOCATION;
  }

  @Override
  public boolean shouldRender(Specter livingEntity, Frustum camera, double camX, double camY, double camZ) {
    return true;
  }

  @Override
  public void render(Specter specter, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

    poseStack.pushPose();

    // shifting the pose slightly copied from BoatRenderer.class
    poseStack.translate(0.0F, 0.0625f, 0.0F);

    // rotating to face look direction copied from BoatRenderer.class
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
    // poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));

    // poseStack.mulPose(Axis.XP.rotationDegrees(180.0F - specter.getViewYRot(1.0f)));
    poseStack.mulPose(Axis.XP.rotationDegrees(180.0f - specter.getViewXRot(1.0f)));

    // flipping x-axis and y-axis copied from BoatRenderer.class
    poseStack.scale(-1.0F, -1.0F, 1.0F);

    this.getModel().setupAnim(specter, partialTick, 0.0F, -0.1F, 0.0F, 0.0F);

    // call to render method defined by our model
    // this.getModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation(specter))), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

    float[] diffuseColors = specter.getDiffuseSpecterColors();
    this.getModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation(specter))), packedLight, OverlayTexture.NO_OVERLAY, diffuseColors[0],
        diffuseColors[1], diffuseColors[2], 1.0f);

    poseStack.popPose();

    super.render(specter, entityYaw, partialTick, poseStack, buffer, packedLight);
  }
}
