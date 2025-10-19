package com.cursee.specter.impl.client.model;

import com.cursee.specter.impl.common.entity.Specter;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SpecterModel<T extends Specter> extends HierarchicalModel<T> {

  private final ModelPart root;
  private final ModelPart body;

  public SpecterModel(final ModelPart root) {
    this.root = root;
    this.body = root.getChild("body");
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshDefinition = new MeshDefinition();
    PartDefinition rootPart = meshDefinition.getRoot();
    PartDefinition bodyPart = rootPart.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 9.0F), PartPose.rotation(-0.1F, 0.0F, 0.0F));
    return LayerDefinition.create(meshDefinition, 64, 64);
  }

  @Override
  public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {

  }
}
