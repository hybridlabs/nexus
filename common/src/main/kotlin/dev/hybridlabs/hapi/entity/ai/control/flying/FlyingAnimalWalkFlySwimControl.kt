package dev.hybridlabs.hapi.entity.ai.control.flying

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.util.Mth
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import kotlin.math.abs
import kotlin.math.sqrt

class FlyingAnimalWalkFlySwimControl(
    bird: BaseFlyingAnimal,
    maxTurn: Int,
    hoversInPlace: Boolean,
) : FlyingMoveControl(bird, maxTurn, hoversInPlace) {
    private val maxTurn = 0
    private val hoversInPlace = false

    override fun tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT
            this.mob.isNoGravity = true
            val d0 = this.wantedX - this.mob.x
            val d1 = this.wantedY - this.mob.y
            val d2 = this.wantedZ - this.mob.z
            val d3 = d0 * d0 + d1 * d1 + d2 * d2
            if (d3 < 2.5000003E-7) {
                this.mob.setYya(0.0f)
                this.mob.setZza(0.0f)
                return
            }

            val f = (Mth.atan2(d2, d0) * (180f / Math.PI.toFloat()).toDouble()).toFloat() - 90.0f
            this.mob.yRot = this.rotlerp(this.mob.yRot, f, 90.0f)
            val f1: Float
            if (this.mob.onGround()) {
                f1 = (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)).toFloat()
            } else {
                f1 = (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED)).toFloat()
            }

            this.mob.speed = f1
            val d4 = sqrt(d0 * d0 + d2 * d2)
            if (abs(d1) > 1.0E-5 || abs(d4) > 1.0E-5) {
                val f2 = (-(Mth.atan2(d1, d4) * (180f / Math.PI.toFloat()).toDouble())).toFloat()
                this.mob.xRot = this.rotlerp(this.mob.xRot, f2, this.maxTurn.toFloat())
                this.mob.setYya(if (d1 > 0.0) f1 else -f1)
            }
        } else {
            if (!this.hoversInPlace) {
                this.mob.isNoGravity = false
            }

            this.mob.setYya(0.0f)
            this.mob.setZza(0.0f)
        }
    }
}