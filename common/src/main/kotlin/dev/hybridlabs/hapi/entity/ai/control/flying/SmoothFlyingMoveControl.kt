package dev.hybridlabs.hapi.entity.ai.control.flying

import net.minecraft.util.Mth
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.MoveControl
import kotlin.math.abs
import kotlin.math.sqrt

open class SmoothFlyingMoveControl(
    bird: Mob,
    private val maxTurnX: Int,
    private val maxTurnY: Int,
    private val flightSpeedModifier: Float,
    private val onLandSpeedModifier: Float,
) : MoveControl(bird) {
    override fun tick() {
        if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone) {
            val d0 = this.wantedX - this.mob.x
            val d1 = this.wantedY - this.mob.y
            val d2 = this.wantedZ - this.mob.z
            val d3 = d0 * d0 + d1 * d1 + d2 * d2
            if (d3 < 2.5000003E-7) {
                this.mob.setZza(0.0f)
            } else {
                val f = (Mth.atan2(d2, d0) * (180f / Math.PI.toFloat()).toDouble()).toFloat() - 90.0f
                this.mob.yRot = this.rotlerp(this.mob.yRot, f, this.maxTurnY.toFloat())
                this.mob.yBodyRot = this.mob.yRot
                this.mob.yHeadRot = this.mob.yRot
                val f1 = (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)).toFloat()
                if (this.mob.isInWater) {
                    this.mob.speed = f1 * this.flightSpeedModifier
                    val d4 = sqrt(d0 * d0 + d2 * d2)
                    if (abs(d1) > 1.0E-5 || abs(d4) > 1.0E-5) {
                        var f3 = -((Mth.atan2(d1, d4) * (180f / Math.PI.toFloat()).toDouble()).toFloat())
                        f3 = Mth.clamp(Mth.wrapDegrees(f3), (-this.maxTurnX).toFloat(), this.maxTurnX.toFloat())
                        this.mob.xRot = this.rotlerp(this.mob.xRot, f3, 5.0f)
                    }

                    val f6 = Mth.cos(this.mob.xRot * (Math.PI.toFloat() / 180f))
                    val f4 = Mth.sin(this.mob.xRot * (Math.PI.toFloat() / 180f))
                    this.mob.zza = f6 * f1
                    this.mob.yya = -f4 * f1
                } else {
                    val f5 = abs(Mth.wrapDegrees(this.mob.yRot - f))
                    val f2 = getTurningSpeedFactor(f5)
                    this.mob.speed = f1 * this.onLandSpeedModifier * f2
                }
            }
        } else {
            this.mob.speed = 0.0f
            this.mob.setXxa(0.0f)
            this.mob.setYya(0.0f)
            this.mob.setZza(0.0f)
        }
    }

    companion object {
        private const val FULL_SPEED_TURN_THRESHOLD = 10.0f
        private const val STOP_TURN_THRESHOLD = 60.0f
        private fun getTurningSpeedFactor(p_249853_: Float): Float {
            return 1.0f - Mth.clamp((p_249853_ - 10.0f) / 50.0f, 0.0f, 1.0f)
        }
    }
}