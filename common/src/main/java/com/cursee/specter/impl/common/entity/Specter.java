package com.cursee.specter.impl.common.entity;

import com.cursee.specter.api.server.specter.SpecterApi;
import com.cursee.specter.impl.common.registry.ModEntities;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;

/// @see net.minecraft.world.entity.monster.Phantom
public class Specter extends FlyingMob implements TraceableEntity {

  public static final float FLAP_DEGREES_PER_TICK = 7.448451F;
  public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);

  private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Specter.class, EntityDataSerializers.INT);

  private Entity owner = null;

  private Vec3 moveTargetPoint = Vec3.ZERO;
  private BlockPos anchorPoint = BlockPos.ZERO;
  private AttackPhase attackPhase = AttackPhase.CIRCLE;

  /// This constructor is used for {@link EntityType} registration in {@link ModEntities}
  public Specter(EntityType<? extends Specter> entityType, Level level) {
    super(entityType, level);
    this.moveControl = new Specter.SpecterMoveControl(this);
    this.lookControl = new SpecterLookControl(this);
  }

  /// Creates a Specter owned by a ServerPlayer for {@link SpecterApi}
  public Specter(ServerLevel level, ServerPlayer player) {
    this(ModEntities.SPECTER, level);
    this.owner = player;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 0);
  }

  public boolean isFlapping() {
    return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
  }

  protected BodyRotationControl createBodyControl() {
    return new Specter.SpecterBodyRotationControl(this);
  }

  protected void registerGoals() {
    this.goalSelector.addGoal(1, new Specter.SpecterAttackStrategyGoal());
    this.goalSelector.addGoal(2, new Specter.SpecterSweepAttackGoal());
    this.goalSelector.addGoal(3, new Specter.SpecterCircleAroundAnchorGoal());
    this.targetSelector.addGoal(1, new Specter.SpecterAttackPlayerTargetGoal());
  }

  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(ID_SIZE, 0);
  }

  private void updateSpecterSizeInfo() {
    this.refreshDimensions();
    AttributeInstance instance = this.getAttribute(Attributes.ATTACK_DAMAGE);
    if (instance != null) {
      instance.setBaseValue(6 + this.getSpecterSize());
    }
  }

  public int getSpecterSize() {
    return this.entityData.get(ID_SIZE);
  }

  public void setSpecterSize(int specterSize) {
    this.entityData.set(ID_SIZE, Mth.clamp(specterSize, 0, 64));
  }

  protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
    return dimensions.height * 0.35F;
  }

  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    if (ID_SIZE.equals(key)) {
      this.updateSpecterSizeInfo();
    }

    super.onSyncedDataUpdated(key);
  }

  public int getUniqueFlapTickOffset() {
    return this.getId() * 3;
  }

  protected boolean shouldDespawnInPeaceful() {
    return false;
  }

  public void tick() {
    super.tick();
    if (this.level().isClientSide) {
      float f = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount) * FLAP_DEGREES_PER_TICK * ((float) Math.PI / 180F) + (float) Math.PI);
      float g = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount + 1) * FLAP_DEGREES_PER_TICK * ((float) Math.PI / 180F) + (float) Math.PI);
      if (f > 0.0F && g <= 0.0F) {
        this.level()
            .playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F,
                false);
      }

      int i = this.getSpecterSize();
      float h = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * (1.3F + 0.21F * (float) i);
      float j = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * (1.3F + 0.21F * (float) i);
      float k = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);
      this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double) h, this.getY() + (double) k, this.getZ() + (double) j, 0.0F, 0.0F, 0.0F);
      this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double) h, this.getY() + (double) k, this.getZ() - (double) j, 0.0F, 0.0F, 0.0F);
    }

  }

  public void aiStep() {
    if (this.isAlive() && this.isSunBurnTick()) {
      this.setSecondsOnFire(8);
    }

    super.aiStep();
  }

  protected void customServerAiStep() {
    super.customServerAiStep();
  }

  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
    this.anchorPoint = this.blockPosition().above(5);
    this.setSpecterSize(0);
    return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
  }

  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    if (compound.contains("AX")) {
      this.anchorPoint = new BlockPos(compound.getInt("AX"), compound.getInt("AY"), compound.getInt("AZ"));
    }

    this.setSpecterSize(compound.getInt("Size"));
  }

  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("AX", this.anchorPoint.getX());
    compound.putInt("AY", this.anchorPoint.getY());
    compound.putInt("AZ", this.anchorPoint.getZ());
    compound.putInt("Size", this.getSpecterSize());
  }

  public boolean shouldRenderAtSqrDistance(double distance) {
    return true;
  }

  public SoundSource getSoundSource() {
    return SoundSource.HOSTILE;
  }

  protected SoundEvent getAmbientSound() {
    return SoundEvents.PHANTOM_AMBIENT;
  }

  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.PHANTOM_HURT;
  }

  protected SoundEvent getDeathSound() {
    return SoundEvents.PHANTOM_DEATH;
  }

  public MobType getMobType() {
    return MobType.UNDEFINED;
  }

  protected float getSoundVolume() {
    return 1.0F;
  }

  public boolean canAttackType(EntityType<?> entityType) {
    return true;
  }

  public EntityDimensions getDimensions(Pose pose) {
    int i = this.getSpecterSize();
    EntityDimensions entityDimensions = super.getDimensions(pose);
    float f = (entityDimensions.width + 0.2F * (float) i) / entityDimensions.width;
    return entityDimensions.scale(f);
  }

  public double getPassengersRidingOffset() {
    return this.getEyeHeight();
  }

  @Override
  public Entity getOwner() {
    return this.owner;
  }

  // ADDITIONAL CLASSES

  enum AttackPhase {
    CIRCLE, SWOOP
  }

  class SpecterAttackPlayerTargetGoal extends Goal {

    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0F);
    private int nextScanTick = reducedTickDelay(20);

    public boolean canUse() {
      if (this.nextScanTick > 0) {
        --this.nextScanTick;
        return false;
      } else {
        this.nextScanTick = reducedTickDelay(60);
        List<Player> list = Specter.this.level().getNearbyPlayers(this.attackTargeting, Specter.this, Specter.this.getBoundingBox().inflate(16.0F, 64.0F, 16.0F));
        if (!list.isEmpty()) {

          // fails, because entity doesn't implement comparable
          // list.<Entity>sort(Comparator.comparing(Entity::getY).reversed());

          list.sort(Comparator.comparing(o -> ((Entity) o).getY()).reversed()); // Comparator#comparing ... mojang why

          for (Player player : list) {
            if (Specter.this.canAttack(player, TargetingConditions.DEFAULT)) {
              Specter.this.setTarget(player);
              return true;
            }
          }
        }

        return false;
      }
    }

    public boolean canContinueToUse() {
      LivingEntity livingentity = Specter.this.getTarget();
      return livingentity != null && Specter.this.canAttack(livingentity, TargetingConditions.DEFAULT);
    }
  }

  class SpecterAttackStrategyGoal extends Goal {

    private int nextSweepTick;

    public boolean canUse() {
      LivingEntity livingentity = Specter.this.getTarget();
      return livingentity != null && Specter.this.canAttack(livingentity, TargetingConditions.DEFAULT);
    }

    public void start() {
      this.nextSweepTick = this.adjustedTickDelay(10);
      Specter.this.attackPhase = Specter.AttackPhase.CIRCLE;
      this.setAnchorAboveTarget();
    }

    public void stop() {
      Specter.this.anchorPoint = Specter.this.level().getHeightmapPos(Types.MOTION_BLOCKING, Specter.this.anchorPoint).above(10 + Specter.this.random.nextInt(20));
    }

    public void tick() {
      if (Specter.this.attackPhase == Specter.AttackPhase.CIRCLE) {
        --this.nextSweepTick;
        if (this.nextSweepTick <= 0) {
          Specter.this.attackPhase = Specter.AttackPhase.SWOOP;
          this.setAnchorAboveTarget();
          this.nextSweepTick = this.adjustedTickDelay((8 + Specter.this.random.nextInt(4)) * 20);
          Specter.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Specter.this.random.nextFloat() * 0.1F);
        }
      }

    }

    private void setAnchorAboveTarget() {
      Specter.this.anchorPoint = Specter.this.getTarget().blockPosition().above(20 + Specter.this.random.nextInt(20));
      if (Specter.this.anchorPoint.getY() < Specter.this.level().getSeaLevel()) {
        Specter.this.anchorPoint = new BlockPos(Specter.this.anchorPoint.getX(), Specter.this.level().getSeaLevel() + 1, Specter.this.anchorPoint.getZ());
      }

    }
  }

  class SpecterBodyRotationControl extends BodyRotationControl {

    public SpecterBodyRotationControl(Mob mob) {
      super(mob);
    }

    public void clientTick() {
      Specter.this.yHeadRot = Specter.this.yBodyRot;
      Specter.this.yBodyRot = Specter.this.getYRot();
    }
  }

  class SpecterCircleAroundAnchorGoal extends Specter.SpecterMoveTargetGoal {

    private float angle;
    private float distance;
    private float height;
    private float clockwise;

    public boolean canUse() {
      return Specter.this.getTarget() == null || Specter.this.attackPhase == Specter.AttackPhase.CIRCLE;
    }

    public void start() {
      this.distance = 5.0F + Specter.this.random.nextFloat() * 10.0F;
      this.height = -4.0F + Specter.this.random.nextFloat() * 9.0F;
      this.clockwise = Specter.this.random.nextBoolean() ? 1.0F : -1.0F;
      this.selectNext();
    }

    public void tick() {
      if (Specter.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
        this.height = -4.0F + Specter.this.random.nextFloat() * 9.0F;
      }

      if (Specter.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
        ++this.distance;
        if (this.distance > 15.0F) {
          this.distance = 5.0F;
          this.clockwise = -this.clockwise;
        }
      }

      if (Specter.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
        this.angle = Specter.this.random.nextFloat() * 2.0F * (float) Math.PI;
        this.selectNext();
      }

      if (this.touchingTarget()) {
        this.selectNext();
      }

      if (Specter.this.moveTargetPoint.y < Specter.this.getY() && !Specter.this.level().isEmptyBlock(Specter.this.blockPosition().below(1))) {
        this.height = Math.max(1.0F, this.height);
        this.selectNext();
      }

      if (Specter.this.moveTargetPoint.y > Specter.this.getY() && !Specter.this.level().isEmptyBlock(Specter.this.blockPosition().above(1))) {
        this.height = Math.min(-1.0F, this.height);
        this.selectNext();
      }

    }

    private void selectNext() {
      if (BlockPos.ZERO.equals(Specter.this.anchorPoint)) {
        Specter.this.anchorPoint = Specter.this.blockPosition();
      }

      this.angle += this.clockwise * 15.0F * ((float) Math.PI / 180F);
      Specter.this.moveTargetPoint = Vec3.atLowerCornerOf(Specter.this.anchorPoint)
          .add(this.distance * Mth.cos(this.angle), -4.0F + this.height, this.distance * Mth.sin(this.angle));
    }
  }

  static class SpecterLookControl extends LookControl {

    public SpecterLookControl(Mob mob) {
      super(mob);
    }

    public void tick() {
    }
  }

  class SpecterMoveControl extends MoveControl {

    private float speed = 0.1F;

    public SpecterMoveControl(Mob mob) {
      super(mob);
    }

    public void tick() {
      if (Specter.this.horizontalCollision) {
        Specter.this.setYRot(Specter.this.getYRot() + 180.0F);
        this.speed = 0.1F;
      }

      double d0 = Specter.this.moveTargetPoint.x - Specter.this.getX();
      double d1 = Specter.this.moveTargetPoint.y - Specter.this.getY();
      double d2 = Specter.this.moveTargetPoint.z - Specter.this.getZ();
      double d3 = Math.sqrt(d0 * d0 + d2 * d2);
      if (Math.abs(d3) > (double) 1.0E-5F) {
        double d4 = (double) 1.0F - Math.abs(d1 * (double) 0.7F) / d3;
        d0 *= d4;
        d2 *= d4;
        d3 = Math.sqrt(d0 * d0 + d2 * d2);
        double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
        float f = Specter.this.getYRot();
        float f1 = (float) Mth.atan2(d2, d0);
        float f2 = Mth.wrapDegrees(Specter.this.getYRot() + 90.0F);
        float f3 = Mth.wrapDegrees(f1 * (180F / (float) Math.PI));
        Specter.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
        Specter.this.yBodyRot = Specter.this.getYRot();
        if (Mth.degreesDifferenceAbs(f, Specter.this.getYRot()) < 3.0F) {
          this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
        } else {
          this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
        }

        float f4 = (float) (-(Mth.atan2(-d1, d3) * (double) (180F / (float) Math.PI)));
        Specter.this.setXRot(f4);
        float f5 = Specter.this.getYRot() + 90.0F;
        double d6 = (double) (this.speed * Mth.cos(f5 * ((float) Math.PI / 180F))) * Math.abs(d0 / d5);
        double d7 = (double) (this.speed * Mth.sin(f5 * ((float) Math.PI / 180F))) * Math.abs(d2 / d5);
        double d8 = (double) (this.speed * Mth.sin(f4 * ((float) Math.PI / 180F))) * Math.abs(d1 / d5);
        Vec3 vec3 = Specter.this.getDeltaMovement();
        Specter.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2)));
      }

    }
  }

  abstract class SpecterMoveTargetGoal extends Goal {

    public SpecterMoveTargetGoal() {
      this.setFlags(EnumSet.of(Flag.MOVE));
    }

    protected boolean touchingTarget() {
      return Specter.this.moveTargetPoint.distanceToSqr(Specter.this.getX(), Specter.this.getY(), Specter.this.getZ()) < (double) 4.0F;
    }
  }

  class SpecterSweepAttackGoal extends Specter.SpecterMoveTargetGoal {

    private static final int CAT_SEARCH_TICK_DELAY = 20;
    private boolean isScaredOfCat;
    private int catSearchTick;

    public boolean canUse() {
      return Specter.this.getTarget() != null && Specter.this.attackPhase == Specter.AttackPhase.SWOOP;
    }

    public boolean canContinueToUse() {
      LivingEntity livingentity = Specter.this.getTarget();
      if (livingentity == null) {
        return false;
      } else if (!livingentity.isAlive()) {
        return false;
      } else {
        if (livingentity instanceof Player player) {
          if (livingentity.isSpectator() || player.isCreative()) {
            return false;
          }
        }

        if (!this.canUse()) {
          return false;
        } else {
          if (Specter.this.tickCount > this.catSearchTick) {
            this.catSearchTick = Specter.this.tickCount + 20;
            List<Cat> list = Specter.this.level().getEntitiesOfClass(Cat.class, Specter.this.getBoundingBox().inflate(16.0F), EntitySelector.ENTITY_STILL_ALIVE);

            for (Cat cat : list) {
              cat.hiss();
            }

            this.isScaredOfCat = !list.isEmpty();
          }

          return !this.isScaredOfCat;
        }
      }
    }

    public void start() {
    }

    public void stop() {
      Specter.this.setTarget(null);
      Specter.this.attackPhase = Specter.AttackPhase.CIRCLE;
    }

    public void tick() {
      LivingEntity livingentity = Specter.this.getTarget();
      if (livingentity != null) {
        Specter.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5F), livingentity.getZ());
        if (Specter.this.getBoundingBox().inflate(0.2F).intersects(livingentity.getBoundingBox())) {
          Specter.this.doHurtTarget(livingentity);
          Specter.this.attackPhase = Specter.AttackPhase.CIRCLE;
          if (!Specter.this.isSilent()) {
            Specter.this.level().levelEvent(1039, Specter.this.blockPosition(), 0);
          }
        } else if (Specter.this.horizontalCollision || Specter.this.hurtTime > 0) {
          Specter.this.attackPhase = Specter.AttackPhase.CIRCLE;
        }
      }
    }
  }
}
