package dev.hybridlabs.hapi.entity.base.flying

import dev.hybridlabs.birds.entity.ai.goal.FlyingAnimalFollowParentGoal
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.control.LookControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathType
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.Nullable
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.constant.DefaultAnimations
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.*

@Suppress("LeakingThis")
open class BaseFlyingAnimal(
    type: EntityType<out BaseFlyingAnimal>,
    world: Level,
) :
    AgeableMob(type, world),
    GeoEntity {
    var inLove = 0
    private var loveCause: UUID? = null
    private val factory = GeckoLibUtil.createInstanceCache(this)
    var fromCreatureNet = false
    var isClipped: Boolean = false

     override fun createNavigation(level: Level): PathNavigation {
        setPathfindingMalus(PathType.WATER, 0.0f)
        setPathfindingMalus(PathType.DANGER_FIRE, 16.0f)
        setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0f)

        moveControl = MoveControl(this)
        navigation = GroundPathNavigation(this, level)
        lookControl = LookControl(this)

        return GroundPathNavigation(this, level)
    }

    override fun customServerAiStep() {
        if (this.getAge() != 0) {
            this.inLove = 0
        }

        super.customServerAiStep()
    }

    override fun aiStep() {
        super.aiStep()

        this.updateSwingTime()

        val vec3d = this.deltaMovement
        if (!this.onGround() && vec3d.y < 0.0) {
            this.deltaMovement = vec3d.multiply(1.0, 0.6, 1.0)
        }

        if (this.getAge() != 0) {
            this.inLove = 0
        }

        if (this.inLove > 0) {
            --this.inLove
            if (this.inLove % 10 == 0) {
                val d0 = this.random.nextGaussian() * 0.02
                val d1 = this.random.nextGaussian() * 0.02
                val d2 = this.random.nextGaussian() * 0.02
                this.level().addParticle(
                    ParticleTypes.HEART,
                    this.getRandomX(1.0),
                    this.randomY + 0.5,
                    this.getRandomZ(1.0),
                    d0,
                    d1,
                    d2
                )
            }
        }
    }

    override fun handleEntityEvent(id: Byte) {
        if (id.toInt() == 45) {
            val itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND)
            if (!itemstack.isEmpty) {
                for (i in 0..7) {
                    val vec3 = (Vec3(
                        (this.random.nextFloat().toDouble() - 0.5) * 0.1,
                        Math.random() * 0.1 + 0.1,
                        0.0
                    )).xRot(-this.xRot * (Math.PI.toFloat() / 180f))
                        .yRot(-this.yRot * (Math.PI.toFloat() / 180f))
                    this.level().addParticle(
                        ItemParticleOption(ParticleTypes.ITEM, itemstack),
                        this.x + this.lookAngle.x / 2.0,
                        this.y,
                        this.z + this.lookAngle.z / 2.0,
                        vec3.x,
                        vec3.y + 0.05,
                        vec3.z
                    )
                }
            }
        }

        if (id.toInt() == 18) {
            for (i in 0..6) {
                val d0 = this.random.nextGaussian() * 0.02
                val d1 = this.random.nextGaussian() * 0.02
                val d2 = this.random.nextGaussian() * 0.02
                this.level().addParticle(
                    ParticleTypes.HEART,
                    this.getRandomX(1.0),
                    this.randomY + 0.5,
                    this.getRandomZ(1.0),
                    d0,
                    d1,
                    d2
                )
            }
        } else {
            super.handleEntityEvent(id)
        }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (this.isInvulnerableTo(source)) {
            return false
        }

        this.inLove = 0
        return super.hurt(source, amount)
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(0, PanicGoal(this, 1.2))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(2, RandomLookAroundGoal(this))
        goalSelector.addGoal(5, FlyingAnimalFollowParentGoal(this, 1.1))
        goalSelector.addGoal(11, LookAtPlayerGoal(this, Player::class.java, 10.0f))
    }

    fun isBelowWaterline(): Boolean {
        return this.isUnderWater || this.getFluidHeight(FluidTags.WATER) > this.getWaterline()
    }

    open fun getWaterline(): Float {
        return 0.4f
    }

    override fun getMaxHeadXRot(): Int {
        return 90
    }

    override fun getMaxHeadYRot(): Int {
        return 90
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("FromCreatureNet", fromCreatureNet)
        compound.putInt("InLove", this.inLove)

        if (this.loveCause != null) {
            compound.putUUID("LoveCause", this.loveCause)
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        fromCreatureNet = compound.getBoolean("FromCreatureNet")
        this.inLove = compound.getInt("InLove")
        this.loveCause = if (compound.hasUUID("LoveCause")) compound.getUUID("LoveCause") else null
    }
    //#endregion

    open fun isFood(stack: ItemStack): Boolean {
        return stack.`is`(Items.WHEAT_SEEDS)
    }

    public override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val itemstack = player.getItemInHand(hand)
        if (this.isFood(itemstack)) {
            val i = this.getAge()
            if (!this.level().isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, itemstack)
                this.setInLove(player)
                return InteractionResult.SUCCESS
            }

            if (this.isBaby) {
                this.usePlayerItem(player, hand, itemstack)
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true)
                return InteractionResult.sidedSuccess(this.level().isClientSide)
            }

            if (this.level().isClientSide) {
                return InteractionResult.CONSUME
            }
        }

        return super.mobInteract(player, hand)
    }

    protected open fun usePlayerItem(player: Player, hand: InteractionHand?, stack: ItemStack) {
        if (!player.abilities.instabuild) {
            stack.shrink(1)
        }
    }

    open fun canFallInLove(): Boolean {
        return this.inLove <= 0
    }

    fun setInLove(player: Player?) {
        this.inLove = 600
        if (player != null) {
            this.loveCause = player.getUUID()
        }

        this.level().broadcastEntityEvent(this, 18.toByte())
    }

    fun isInLove(): Boolean {
        return this.inLove > 0
    }

    fun resetLove() {
        this.inLove = 0
    }

    fun canMate(otherWaterAnimal: BaseFlyingAnimal): Boolean {
        return if (otherWaterAnimal === this) {
            false
        } else if (otherWaterAnimal.javaClass != this.javaClass) {
            false
        } else {
            this.isInLove() && otherWaterAnimal.isInLove()
        }
    }

    open fun spawnChildFromBreeding(level: ServerLevel, mate: BaseFlyingAnimal) {
        val baby = this.getBreedOffspring(level, mate) ?: return

        this.setPersistenceRequired()
        mate.setPersistenceRequired()
        baby.setPersistenceRequired()
        baby.isBaby = true
        baby.moveTo(this.x, this.y, this.z, 0.0f, 0.0f)

        this.finalizeSpawnChildFromBreeding(level, mate)
        level.addFreshEntityWithPassengers(baby)
    }

    fun finalizeSpawnChildFromBreeding(level: ServerLevel, bird: BaseFlyingAnimal) {
        this.setAge(6000)
        bird.setAge(6000)
        this.resetLove()
        bird.resetLove()
        level.broadcastEntityEvent(this, 18.toByte())
        if (level.gameRules.getBoolean(GameRules.RULE_DOMOBLOOT)) {
            level.addFreshEntity(
                ExperienceOrb(
                    level,
                    this.x,
                    this.y,
                    this.z,
                    this.getRandom().nextInt(7) + 1
                )
            )
        }
    }

    @Nullable
    override fun getBreedOffspring(serverLevel: ServerLevel, ageableMob: AgeableMob): AgeableMob? {
        throw NotImplementedError("Breeding is not implemented")
    }

    override fun registerControllers(controllerRegistrar: AnimatableManager.ControllerRegistrar) {
        controllerRegistrar.add(
            AnimationController(
                this, "Walk/Swim/Fly/Idle", 4
            ) { state: AnimationState<BaseFlyingAnimal> ->
                when {
                    state.isMoving && onGround() -> state.setAndContinue(DefaultAnimations.WALK)
                    !this.onGround() && !isInWater -> state.setAndContinue(DefaultAnimations.FLY)
                    else -> state.setAndContinue(DefaultAnimations.IDLE)
                }
            }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return factory
    }

    override fun removeWhenFarAway(distanceSquared: Double): Boolean {
        return !isPersistenceRequired &&
                !this.fromCreatureNet &&
                !this.hasCustomName()
    }

    override fun getMaxSpawnClusterSize(): Int {
        return 2
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.PARROT_HURT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.PARROT_DEATH
    }

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.PARROT_AMBIENT
    }

    override fun checkFallDamage(
        heightDifference: Double,
        onGround: Boolean,
        state: BlockState,
        landedPosition: BlockPos,
    ) {
    }

    companion object {
        @Suppress("UNUSED_PARAMETER")
        private fun isBrightEnoughToSpawn(level: BlockAndTintGetter, pos: BlockPos): Boolean {
            return level.getRawBrightness(pos, 0) > 8
        }

        fun canBirdSpawn(
            type: EntityType<out BaseFlyingAnimal>,
            level: LevelAccessor,
            reason: MobSpawnType,
            pos: BlockPos,
            random: RandomSource,
        ): Boolean {
            return isBrightEnoughToSpawn(level, pos) &&
                    level.getBlockState(pos.below()).isSolid &&
                    level.canSeeSky(pos)
        }
    }
}