package dev.hybridlabs.hapi.entity.base.vehicle

import net.minecraft.BlockUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.world.entity.*
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.WaterlilyBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.Shapes
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.constant.DefaultAnimations
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.math.max
import kotlin.math.sin

abstract class BaseBoatEntity(
    type: EntityType<out BaseBoatEntity>,
    world: Level,
) :
    Entity(type, world),
    GeoEntity {
    private val animCache = GeckoLibUtil.createInstanceCache(this)

    private var isAboveBubbleColumn = false
    private var bubbleColumnDirectionIsDown = false
    private var bubbleMultiplier = 0f
    private var bubbleAngle = 0f
    private var bubbleAngleO = 0f

    var outOfControlTicks = 0f

    private var waterLevel = 0.0
    private var landFriction = 0f
    private var invFriction = 0f

    var deltaRotation = 0f
    private var lerpSteps = 0
    private var lerpX = 0.0
    private var lerpY = 0.0
    private var lerpZ = 0.0
    private var lerpYRot = 0.0
    private var lerpXRot = 0.0
    var lastYd = 0.0

    var status: Status? = null
    private var oldStatus: Status? = null

    init {
        noCulling = true
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController(this, "Boat Controller", 4) { state ->
                when {
                    isInWater -> {
                        state.setAndContinue(DefaultAnimations.IDLE)
                    }

                    else -> {
                        state.setAndContinue(DefaultAnimations.IDLE)
                    }
                }
            }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return animCache
    }

    override fun defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0)
        this.entityData.define(DATA_ID_HURTDIR, 1)
        this.entityData.define(DATA_ID_DAMAGE, 0.0f)
        this.entityData.define(DATA_ID_BUBBLE_TIME, 0)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        tag.putFloat("Damage", getDamage())
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        setDamage(tag.getFloat("Damage"))
    }

    fun setDamage(damageTaken: Float) {
        this.entityData.set(DATA_ID_DAMAGE, damageTaken)
    }

    fun getDamage(): Float {
        return this.entityData.get(DATA_ID_DAMAGE)
    }

    fun setHurtTime(hurtTime: Int) {
        this.entityData.set(DATA_ID_HURT, hurtTime)
    }

    fun getHurtTime(): Int {
        return this.entityData.get(DATA_ID_HURT) as Int
    }

    override fun getEyeHeight(pose: Pose, size: EntityDimensions): Float {
        return size.height
    }

    override fun getMovementEmission(): MovementEmission {
        return MovementEmission.EVENTS
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return canVehicleCollide(this, entity)
    }

    override fun canBeCollidedWith(): Boolean {
        return true
    }

    override fun isPushable(): Boolean {
        return true
    }

    override fun onAboveBubbleCol(downwards: Boolean) {
        if (!this.level().isClientSide) {
            this.isAboveBubbleColumn = true
            this.bubbleColumnDirectionIsDown = downwards
            if (this.bubbleTime == 0) {
                this.bubbleTime = 60
            }
        }

        this.level().addParticle(
            ParticleTypes.SPLASH,
            this.x + this.random.nextFloat().toDouble(),
            this.y + 0.7,
            this.z + this.random.nextFloat().toDouble(),
            0.0,
            0.0,
            0.0
        )
        if (this.random.nextInt(20) == 0) {
            this.level().playLocalSound(
                this.x,
                this.y,
                this.z,
                this.swimSplashSound,
                this.soundSource,
                1.0f,
                0.8f + 0.4f * this.random.nextFloat(),
                false
            )
            this.gameEvent(GameEvent.SPLASH, this.controllingPassenger)
        }
    }

    override fun tick() {
        super.tick()

        this.oldStatus = this.status
        this.status = this.getCurrentStatus()

        if (this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0f
        } else {
            ++this.outOfControlTicks
        }

        if (!this.level().isClientSide && this.outOfControlTicks >= 60.0f) {
            this.ejectPassengers()
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1)
        }

        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f)
        }

        this.tickLerp()

        if (this.isControlledByLocalInstance) {

            this.floatWaterVehicle()

            this.move(MoverType.SELF, this.deltaMovement)
        } else {
            this.deltaMovement = Vec3.ZERO
        }

        this.tickBubbleColumn()

        this.checkInsideBlocks()
        val list = this.level()
            .getEntities(this, this.boundingBox.inflate(0.2, -0.01, 0.2), EntitySelector.pushableBy(this))
        if (!list.isEmpty()) {
            val flag = !this.level().isClientSide && this.controllingPassenger !is Player

            for (j in list.indices) {
                val entity = list[j] as Entity
                if (!entity.hasPassenger(this)) {
                    if (flag && this.passengers.size < this.maxPassengers && !entity.isPassenger && this.hasEnoughSpaceFor(
                            entity
                        ) && entity is LivingEntity && (entity !is WaterAnimal) && (entity !is Player)
                    ) {
                        entity.startRiding(this)
                    } else {
                        this.push(entity)
                    }
                }
            }
        }
    }

    fun hasEnoughSpaceFor(entity: Entity): Boolean {
        return entity.bbWidth < this.bbWidth
    }

    protected open val maxPassengers: Int
        get() = 3

    private fun tickBubbleColumn() {
        if (this.level().isClientSide) {
            val i = this.bubbleTime
            if (i > 0) {
                this.bubbleMultiplier += 0.05f
            } else {
                this.bubbleMultiplier -= 0.1f
            }

            this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0f, 1.0f)
            this.bubbleAngleO = this.bubbleAngle
            this.bubbleAngle =
                10.0f * sin((0.5f * this.level().gameTime.toFloat()).toDouble()).toFloat() * this.bubbleMultiplier
        } else {
            if (!this.isAboveBubbleColumn) {
                this.bubbleTime = 0
            }

            var k = this.bubbleTime
            if (k > 0) {
                --k
                this.bubbleTime = k
                val j = 60 - k - 1
                if (j > 0 && k == 0) {
                    this.bubbleTime = 0
                    val vec3 = this.deltaMovement
                    if (this.bubbleColumnDirectionIsDown) {
                        this.deltaMovement = vec3.add(0.0, -0.7, 0.0)
                        this.ejectPassengers()
                    } else {
                        this.setDeltaMovement(
                            vec3.x,
                            if (this.hasPassenger { entity: Entity? -> entity is Player }) 2.7 else 0.6,
                            vec3.z
                        )
                    }
                }

                this.isAboveBubbleColumn = false
            }
        }
    }

    protected val paddleSound: SoundEvent?
        get() {
            return when (this.getCurrentStatus().ordinal) {
                0, 1, 2 -> SoundEvents.BOAT_PADDLE_WATER
                3 -> SoundEvents.BOAT_PADDLE_LAND
                4 -> null
                else -> null
            }
        }

    override fun isPickable(): Boolean {
        return !this.isRemoved
    }

    override fun getRelativePortalPosition(axis: Direction.Axis, portal: BlockUtil.FoundRectangle): Vec3 {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portal))
    }

    override fun lerpTo(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        posRotationIncrements: Int,
        teleport: Boolean,
    ) {
        this.lerpX = x
        this.lerpY = y
        this.lerpZ = z
        this.lerpYRot = yaw.toDouble()
        this.lerpXRot = pitch.toDouble()
        this.lerpSteps = 10
    }

    override fun getMotionDirection(): Direction {
        return this.direction.clockWise
    }

    private fun tickLerp() {
        if (this.isControlledByLocalInstance) {
            this.lerpSteps = 0
            this.syncPacketPositionCodec(this.x, this.y, this.z)
        }

        if (this.lerpSteps > 0) {
            val d0 = this.x + (this.lerpX - this.x) / this.lerpSteps.toDouble()
            val d1 = this.y + (this.lerpY - this.y) / this.lerpSteps.toDouble()
            val d2 = this.z + (this.lerpZ - this.z) / this.lerpSteps.toDouble()
            val d3 = Mth.wrapDegrees(this.lerpYRot - this.yRot.toDouble())
            this.yRot += d3.toFloat() / this.lerpSteps.toFloat()
            this.xRot += (this.lerpXRot - this.xRot.toDouble()).toFloat() / this.lerpSteps.toFloat()
            --this.lerpSteps
            this.setPos(d0, d1, d2)
            this.setRot(this.yRot, this.xRot)
        }
    }

    fun floatWaterVehicle() {
        val gravity = 0.04
        var vspeed: Double = if (this.isNoGravity) 0.0 else -gravity
        var buoyancy = 0.0
        var invFriction = 0.05f
        if (this.oldStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
            this.waterLevel = this.getY(1.0)
            val targetY: Double = this.waterLevelAbove - this.bbHeight + 0.101
            if (this.level().noCollision(this, this.boundingBox.move(0.0, targetY - this.getY(), 0.0))) {
                this.setPos(this.x, targetY, this.z)
                this.deltaMovement = this.deltaMovement.multiply(1.0, 0.0, 1.0)
                this.lastYd = 0.0
            }

            this.status = Status.IN_WATER
        } else {
            if (this.status == Status.IN_WATER) {
                buoyancy = (this.waterLevel - this.y) / this.bbHeight
                invFriction = 0.9f
            } else if (this.status == Status.UNDER_FLOWING_WATER) {
                vspeed = -7.0E-4
                invFriction = 0.9f
            } else if (this.status == Status.UNDER_WATER) {
                buoyancy = 0.01
                invFriction = 0.45f
            } else if (this.status == Status.IN_AIR) {
                invFriction = 0.9f
            } else if (this.status == Status.ON_LAND) {
                invFriction = this.landFriction
                if (this.getControllingPassenger() is Player) {
                    this.landFriction /= 2.0f
                }
            }

            val movement = this.deltaMovement
            this.setDeltaMovement(movement.x * invFriction, movement.y + vspeed, movement.z * invFriction)
            this.deltaRotation *= invFriction
            if (buoyancy > 0.0) {
                val deltaMovement = this.deltaMovement
                this.setDeltaMovement(
                    deltaMovement.x,
                    (deltaMovement.y + buoyancy * (gravity / 0.65)) * 0.75,
                    deltaMovement.z
                )
            }
        }
    }

    private fun getCurrentStatus(): Status {
        val boatStatus = this.isUnderwater
        if (boatStatus != null) {
            this.waterLevel = this.boundingBox.maxY
            return boatStatus
        } else if (this.checkInWater()) {
            return Status.IN_WATER
        } else {
            val f = this.groundFriction
            if (f > 0.0f) {
                this.landFriction = f
                return Status.ON_LAND
            } else {
                return Status.IN_AIR
            }
        }
    }

    val waterLevelAbove: Float
        get() {
            val aabb = this.boundingBox
            val i = Mth.floor(aabb.minX)
            val j = Mth.ceil(aabb.maxX)
            val k = Mth.floor(aabb.maxY)
            val l = Mth.ceil(aabb.maxY - this.lastYd)
            val i1 = Mth.floor(aabb.minZ)
            val j1 = Mth.ceil(aabb.maxZ)
            val mutableBlockPos = BlockPos.MutableBlockPos()

            label39@ for (k1 in k..<l) {
                var f = 0.0f

                for (l1 in i..<j) {
                    for (i2 in i1..<j1) {
                        mutableBlockPos.set(l1, k1, i2)
                        val fluidstate = this.level().getFluidState(mutableBlockPos)
                        if (fluidstate.`is`(FluidTags.WATER)) {
                            f = max(f, fluidstate.getHeight(this.level(), mutableBlockPos))
                        }

                        if (f >= 1.0f) {
                            continue@label39
                        }
                    }
                }

                if (f < 1.0f) {
                    return mutableBlockPos.y.toFloat() + f
                }
            }

            return (l + 1).toFloat()
        }

    val groundFriction: Float
        get() {
            val aabb = this.boundingBox
            val aabb1 = AABB(aabb.minX, aabb.minY - 0.001, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ)
            val i = Mth.floor(aabb1.minX) - 1
            val j = Mth.ceil(aabb1.maxX) + 1
            val k = Mth.floor(aabb1.minY) - 1
            val l = Mth.ceil(aabb1.maxY) + 1
            val i1 = Mth.floor(aabb1.minZ) - 1
            val j1 = Mth.ceil(aabb1.maxZ) + 1
            val voxelshape = Shapes.create(aabb1)
            var f = 0.0f
            var k1 = 0
            val mutableBlockPos = BlockPos.MutableBlockPos()

            for (l1 in i..<j) {
                for (i2 in i1..<j1) {
                    val j2 = (if (l1 != i && l1 != j - 1) 0 else 1) + (if (i2 != i1 && i2 != j1 - 1) 0 else 1)
                    if (j2 != 2) {
                        for (k2 in k..<l) {
                            if (j2 <= 0 || k2 != k && k2 != l - 1) {
                                mutableBlockPos.set(l1, k2, i2)
                                val blockstate = this.level().getBlockState(mutableBlockPos)
                                if (blockstate.block !is WaterlilyBlock && Shapes.joinIsNotEmpty(
                                        blockstate.getCollisionShape(
                                            this.level(),
                                            mutableBlockPos
                                        ).move(l1.toDouble(), k2.toDouble(), i2.toDouble()), voxelshape, BooleanOp.AND
                                    )
                                ) {
                                    f += blockstate.block.getFriction()
                                    ++k1
                                }
                            }
                        }
                    }
                }
            }

            return f / k1.toFloat()
        }

    private fun checkInWater(): Boolean {
        val aabb = this.boundingBox
        val i = Mth.floor(aabb.minX)
        val j = Mth.ceil(aabb.maxX)
        val k = Mth.floor(aabb.minY)
        val l = Mth.ceil(aabb.minY + 0.001)
        val i1 = Mth.floor(aabb.minZ)
        val j1 = Mth.ceil(aabb.maxZ)
        var flag = false
        this.waterLevel = -Double.MAX_VALUE
        val mutableBlockPos = BlockPos.MutableBlockPos()

        for (k1 in i..<j) {
            for (l1 in k..<l) {
                for (i2 in i1..<j1) {
                    mutableBlockPos.set(k1, l1, i2)
                    val fluidstate = this.level().getFluidState(mutableBlockPos)
                    if (fluidstate.`is`(FluidTags.WATER)) {
                        val f = l1.toFloat() + fluidstate.getHeight(this.level(), mutableBlockPos)
                        this.waterLevel = max(f.toDouble(), this.waterLevel)
                        flag = flag or (aabb.minY < f.toDouble())
                    }
                }
            }
        }

        return flag
    }

    private val isUnderwater: Status?
        get() {
            val aabb = this.boundingBox
            val d0 = aabb.maxY + 0.001
            val i = Mth.floor(aabb.minX)
            val j = Mth.ceil(aabb.maxX)
            val k = Mth.floor(aabb.maxY)
            val l = Mth.ceil(d0)
            val i1 = Mth.floor(aabb.minZ)
            val j1 = Mth.ceil(aabb.maxZ)
            var flag = false
            val mutableBlockPos = BlockPos.MutableBlockPos()

            for (k1 in i..<j) {
                for (l1 in k..<l) {
                    for (i2 in i1..<j1) {
                        mutableBlockPos.set(k1, l1, i2)
                        val fluidstate = this.level().getFluidState(mutableBlockPos)
                        if (fluidstate.`is`(FluidTags.WATER) && d0 < (mutableBlockPos.y
                                .toFloat() + fluidstate.getHeight(this.level(), mutableBlockPos)).toDouble()
                        ) {
                            if (!fluidstate.isSource) {
                                return Status.UNDER_FLOWING_WATER
                            }

                            flag = true
                        }
                    }
                }
            }

            return if (flag) Status.UNDER_WATER else null
        }

    override fun checkFallDamage(y: Double, onGround: Boolean, state: BlockState, pos: BlockPos) {
        this.lastYd = this.deltaMovement.y
        if (!this.isPassenger) {
            if (onGround) {
                if (this.fallDistance > 3.0f) {
                    if (this.status != Status.ON_LAND) {
                        this.resetFallDistance()
                        return
                    }

                    this.causeFallDamage(this.fallDistance, 1.0f, this.damageSources().fall())
                    if (!this.level().isClientSide && !this.isRemoved) {
                        this.kill()
                        if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (j in 0..1) {
                                this.spawnAtLocation(Items.STICK)
                            }
                        }
                    }
                }

                this.resetFallDistance()
            } else if (!this.level().getFluidState(this.blockPosition().below()).`is`(FluidTags.WATER) && y < 0.0) {
                this.fallDistance -= y.toFloat()
            }
        }
    }

    private var bubbleTime: Int
        get() = this.entityData.get(DATA_ID_BUBBLE_TIME) as Int
        private set(bubbleTime) {
            this.entityData.set(DATA_ID_BUBBLE_TIME, bubbleTime)
        }

    override fun canAddPassenger(passenger: Entity): Boolean {
        return this.passengers.size < this.maxPassengers && !this.isEyeInFluid(FluidTags.WATER)
    }

    override fun isUnderWater(): Boolean {
        return this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER
    }

    init {
        this.blocksBuilding = true
    }

    enum class Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR
    }

    companion object {
        private val DATA_ID_HURT: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BaseBoatEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_HURTDIR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BaseBoatEntity::class.java, EntityDataSerializers.INT)
        private val DATA_ID_DAMAGE: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(BaseBoatEntity::class.java, EntityDataSerializers.FLOAT)
        private val DATA_ID_BUBBLE_TIME: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BaseBoatEntity::class.java, EntityDataSerializers.INT)

        fun canVehicleCollide(vehicle: Entity, entity: Entity): Boolean {
            return (entity.canBeCollidedWith() || entity.isPushable) && !vehicle.isPassengerOfSameVehicle(entity)
        }
    }
}