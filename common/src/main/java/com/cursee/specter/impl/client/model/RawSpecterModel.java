package com.cursee.specter.impl.client.model;

import com.cursee.specter.impl.common.entity.AbstractRawSpecter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class RawSpecterModel extends EntityModel<AbstractRawSpecter> {

  private final ModelPart root;
  private final ModelPart body;

  public RawSpecterModel(final ModelPart root) {
    this.root = root;
    this.body = root.getChild("body");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshDefinition = new MeshDefinition();
    PartDefinition rootPart = meshDefinition.getRoot();
    PartDefinition bodyPart = rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
    return LayerDefinition.create(meshDefinition, 64, 64);
  }

  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(@NotNull AbstractRawSpecter specter, float v, float v1, float v2, float v3, float v4) {

  }

  @Override
  public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int light, int overlay, float r, float g, float b, float a) {
    this.root().render(poseStack, vertexConsumer, light, overlay, r, g, b, a);
  }
}
