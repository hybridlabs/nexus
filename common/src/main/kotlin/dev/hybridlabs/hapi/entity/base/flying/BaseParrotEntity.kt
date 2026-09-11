package dev.hybridlabs.hapi.entity.base.flying

import dev.hybridlabs.hapi.entity.base.flying.BaseRatiteEntity.Companion.BREEDING_INGREDIENT
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.animal.FlyingAnimal
import net.minecraft.world.entity.animal.ShoulderRidingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import org.jetbrains.annotations.Nullable
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.constant.DefaultAnimations
import software.bernie.geckolib.util.GeckoLibUtil

@Suppress("LeakingThis", "DEPRECATION")
open class BaseParrotEntity(
    type: EntityType<out BaseParrotEntity>,
    world: Level,
    private val isTameable: Boolean
) :
    ShoulderRidingEntity(type, world),
    FlyingAnimal,
    GeoEntity {
    private val factory = GeckoLibUtil.createInstanceCache(this)

    init {
        moveControl = FlyingMoveControl(this, 10, false)
        navigation = FlyingPathNavigation(this, world)
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, PanicGoal(this, 1.1))
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, SitWhenOrderedToGoal(this))
        goalSelector.addGoal(2, FollowOwnerGoal(this, 1.0, 5.0f, 1.0f))
        goalSelector.addGoal(2, WaterAvoidingRandomFlyingGoal(this, 1.0))
        goalSelector.addGoal(2, LookAtPlayerGoal(this, Player::class.java, 8.0f))
    }

    override fun doPush(entity: Entity) {
        if (entity !is Player) {
            super.doPush(entity)
        }
    }

    override fun isFood(stack: ItemStack): Boolean {
        return BREEDING_INGREDIENT.test(stack)
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)

        if (this.isTameable && !this.isTame && TAME_FOOD.contains(itemStack.item)) {
            if (!player.abilities.instabuild) {
                itemStack.shrink(1)
            }

            if (!this.isSilent) {
                this.level().playSound(
                    null,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.PARROT_EAT,
                    this.soundSource,
                    1.0f,
                    1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f
                )
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(10) == 0) {
                    this.tame(player)
                    this.level().broadcastEntityEvent(this, 7.toByte())
                } else {
                    this.level().broadcastEntityEvent(this, 6.toByte())
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide)
        }

        if (!this.isFlying && this.isTame && this.isOwnedBy(player)) {
            if (!this.level().isClientSide) {
                this.isOrderedToSit = !this.isOrderedToSit
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide)
        }

        return super.mobInteract(player, hand)
    }

    @Nullable
    override fun getBreedOffspring(serverLevel: ServerLevel, ageableMob: AgeableMob): AgeableMob? {
        return null
    }

    override fun registerControllers(controllerRegistrar: AnimatableManager.ControllerRegistrar) {
        controllerRegistrar.add(
            AnimationController(
                this, "Walk/Fly/Idle", 4
            ) { state: AnimationState<BaseParrotEntity> ->
                when {
                    state.isMoving && onGround() -> state.setAndContinue(DefaultAnimations.WALK)
                    !this.onGround() -> state.setAndContinue(DefaultAnimations.FLY)
                    else -> state.setAndContinue(DefaultAnimations.IDLE)
                }
            }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return factory
    }

    override fun removeWhenFarAway(distanceSquared: Double): Boolean {
        return !this.hasCustomName()
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
        landedPosition: BlockPos
    ) {
    }

    override fun aiStep() {
        super.aiStep()
        val vec3d = this.deltaMovement
        if (!this.onGround() && vec3d.y < 0.0) {
            this.deltaMovement = vec3d.multiply(1.0, 0.6, 1.0)
        }
    }

    override fun isFlying(): Boolean {
        return !this.onGround()
    }

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun canBirdSpawn(
            type: EntityType<out BaseFlyingAnimal>,
            level: LevelAccessor,
            reason: MobSpawnType,
            pos: BlockPos,
            random: RandomSource,
        ): Boolean {
            return isBrightEnoughToSpawn(level, pos) &&
                    level.getBlockState(pos.below()).isSolid &&
                    level.isEmptyBlock(pos) &&
                    level.canSeeSky(pos)
        }
    }

    val TAME_FOOD: Set<Item> = setOf(
        Items.WHEAT_SEEDS,
        Items.MELON_SEEDS,
        Items.PUMPKIN_SEEDS,
        Items.BEETROOT_SEEDS,
        Items.TORCHFLOWER_SEEDS,
        Items.PITCHER_POD
    )
}