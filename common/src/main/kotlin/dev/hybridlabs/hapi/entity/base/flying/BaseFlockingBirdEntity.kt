package dev.hybridlabs.hapi.entity.base.flying

import dev.hybridlabs.hapi.entity.ai.control.flying.SmoothFlyingMoveControl
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.animal.FlyingAnimal
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathType
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity

open class BaseFlockingBirdEntity(
    type: EntityType<out BaseFlockingBirdEntity>,
    world: Level,
) :
    BaseFlyingAnimal(type, world),
    GeoEntity, FlyingAnimal {

    override fun createNavigation(level: Level): PathNavigation {
        setPathfindingMalus(PathType.WATER, 0.0f)
        setPathfindingMalus(PathType.DANGER_FIRE, 16.0f)
        setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0f)

        moveControl = SmoothFlyingMoveControl(this, 85, 5, 0.02F, 0.1f)
        lookControl = SmoothSwimmingLookControl(this, 10)
        navigation = FlyingPathNavigation(this, level)

        return FlyingPathNavigation(this, level)
    }

    override fun isFlying(): Boolean {
        return !this.onGround() || !this.isInWater
    }

    override fun checkFallDamage(heightDifference: Double, onGround: Boolean, state: BlockState, landedPosition: BlockPos) {
    }

    override fun travel(travelVector: Vec3) {
        if (this.isClipped) {
            super.travel(travelVector)
            return
        }

        if (this.isControlledByLocalInstance) {
            if (this.isInWater) {
                this.moveRelative(0.02f, travelVector)
                this.move(MoverType.SELF, this.deltaMovement)
                this.deltaMovement = this.deltaMovement.scale(0.8)
            } else if (this.isInLava) {
                this.moveRelative(0.02f, travelVector)
                this.move(MoverType.SELF, this.deltaMovement)
                this.deltaMovement = this.deltaMovement.scale(0.5)
            } else {
                var f = 0.91f
                if (this.onGround()) {
                    f = this.level().getBlockState(this.blockPosBelowThatAffectsMyMovement).block
                        .getFriction() * 0.91f
                }

                val f1 = 0.16277137f / (f * f * f)
                f = 0.91f
                if (this.onGround()) {
                    f = this.level().getBlockState(this.blockPosBelowThatAffectsMyMovement).block
                        .getFriction() * 0.91f
                }

                this.moveRelative(if (this.onGround()) 0.1f * f1 else 0.02f, travelVector)
                this.move(MoverType.SELF, this.deltaMovement)
                this.deltaMovement = this.deltaMovement.scale(f.toDouble())
            }
        }

        this.calculateEntityAnimation(false)
    }

    override fun onClimbable(): Boolean {
        return false
    }
}