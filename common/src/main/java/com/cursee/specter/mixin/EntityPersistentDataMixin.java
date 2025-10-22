package com.cursee.specter.mixin;

import com.cursee.specter.api.common.accessor.PersistentDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Implements {@link PersistentDataHolder} on {@link Entity}, so instances can be safely cast at runtime.
@Mixin(Entity.class)
public class EntityPersistentDataMixin implements PersistentDataHolder {

  @Unique
  private static final String SPECTER$PERSISTENT_DATA_TAG = "specter.persistent_data";

  @Unique
  private CompoundTag specter$persistentData;

  @Override
  public CompoundTag specter$getPersistentData() {
    if (this.specter$persistentData == null) {
      this.specter$persistentData = new CompoundTag();
    }
    return this.specter$persistentData;
  }

  @Override
  public void specter$setPersistentData(CompoundTag compoundTag) {
    this.specter$persistentData = compoundTag;
  }

  @Inject(at = @At("TAIL"), method = "load")
  private void specter$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
    if (compound.contains(SPECTER$PERSISTENT_DATA_TAG)) {
      this.specter$persistentData = compound.getCompound(SPECTER$PERSISTENT_DATA_TAG);
    }
  }

  @Inject(at = @At("TAIL"), method = "saveWithoutId")
  private void specter$addAdditionalSaveData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
    if (this.specter$persistentData != null) {
      compound.put(SPECTER$PERSISTENT_DATA_TAG, this.specter$persistentData);
    }
  }
}
