package dev.hybridlabs.birds.entity.ai.goal

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.world.entity.ai.goal.Goal

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class FlyingAnimalFollowParentGoal(
    private val bird: BaseFlyingAnimal,
    private val speedModifier: Double,
) : Goal() {
    private var parent: BaseFlyingAnimal? = null
    private var timeToRecalcPath = 0

    override fun canUse(): Boolean {
        if (this.bird.getAge() >= 0) {
            return false
        } else {
            val list = this.bird.level()
                .getEntitiesOfClass(this.bird.javaClass, this.bird.boundingBox.inflate(8.0, 4.0, 8.0))
            var waterAnimal: BaseFlyingAnimal? = null
            var d0 = Double.MAX_VALUE

            for (waterAnimal1 in list) {
                if (waterAnimal1.getAge() >= 0) {
                    val d1 = this.bird.distanceToSqr(waterAnimal1)
                    if (!(d1 > d0)) {
                        d0 = d1
                        waterAnimal = waterAnimal1
                    }
                }
            }

            if (waterAnimal == null) {
                return false
            } else if (d0 < 9.0) {
                return false
            } else {
                this.parent = waterAnimal
                return true
            }
        }
    }

    override fun canContinueToUse(): Boolean {
        if (this.bird.getAge() >= 0) {
            return false
        } else if (!this.parent!!.isAlive) {
            return false
        } else {
            val d0 = this.bird.distanceToSqr(this.parent)
            return !(d0 < 9.0) && !(d0 > 256.0)
        }
    }

    override fun start() {
        this.timeToRecalcPath = 0
    }

    override fun stop() {
        this.parent = null
    }

    override fun tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10)
            this.bird.getNavigation().moveTo(this.parent, this.speedModifier)
        }
    }
}