package com.cursee.specter.impl.client.model;

import com.cursee.specter.impl.common.entity.AbstractRawSpecter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class RawSpecterModel extends EntityModel<AbstractRawSpecter> {

  private final ModelPart root;
  private final ModelPart body;
  private final ModelPart shell;

  private boolean shouldRenderShell = false;

  public RawSpecterModel(final ModelPart root) {
    this.root = root;
    this.body = root.getChild("body");
    this.shell = root.getChild("shell");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

    PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

    PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

    PartDefinition shell = partdefinition.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(@NotNull AbstractRawSpecter specter, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    if (specter.getOwner() != null && specter.distanceTo(specter.getOwner()) < 4.0f) {
      this.shouldRenderShell = true;
    } else {
      this.shouldRenderShell = false;
    }
  }

  @Override
  public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int light, int overlay, float r, float g, float b, float a) {
    // this.root().render(poseStack, vertexConsumer, light, overlay, r, g, b, a);

    this.body.render(poseStack, vertexConsumer, light, overlay, r, g, b, a);

    if (this.shouldRenderShell) {
      this.shell.render(poseStack, vertexConsumer, light, overlay, r, g, b, a);
    }
  }
}
