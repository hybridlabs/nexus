package dev.hybridlabs.birds.entity.ai.goal

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.level.Level
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class FlyingAnimalBreedGoal @JvmOverloads constructor(
    protected val bird: BaseFlyingAnimal,
    private val speedModifier: Double,
    private val partnerClass: Class<out BaseFlyingAnimal> = bird.javaClass,
) : Goal() {
    protected val level: Level = bird.level()
    protected var partner: BaseFlyingAnimal? = null
    private var loveTime = 0

    init {
        this.flags = EnumSet.of<Flag?>(Flag.MOVE, Flag.LOOK)
    }

    override fun canUse(): Boolean {
        if (!this.bird.isInLove()) {
            return false
        } else {
            this.partner = this.freePartner
            return this.partner != null
        }
    }

    override fun canContinueToUse(): Boolean {
        return this.partner!!.isAlive && this.partner!!.isInLove() && this.loveTime < 60
    }

    override fun stop() {
        this.partner = null
        this.loveTime = 0
    }

    override fun tick() {
        this.bird.getLookControl().setLookAt(this.partner, 10.0f, this.bird.maxHeadXRot.toFloat())
        this.bird.getNavigation().moveTo(this.partner, this.speedModifier)
        ++this.loveTime
        if (this.loveTime >= this.adjustedTickDelay(60) && this.bird.distanceToSqr(this.partner) < 9.0) {
            this.breed()
        }
    }

    private val freePartner: BaseFlyingAnimal?
        get() {
            val list: MutableList<out BaseFlyingAnimal> = this.level.getNearbyEntities(
                this.partnerClass,
                PARTNER_TARGETING,
                this.bird,
                this.bird.boundingBox.inflate(8.0)
            )
            var d0 = Double.MAX_VALUE
            var bird: BaseFlyingAnimal? = null

            for (bird1 in list) {
                if (this.bird.canMate(bird1) && this.bird.distanceToSqr(bird1) < d0) {
                    bird = bird1
                    d0 = this.bird.distanceToSqr(bird1)
                }
            }

            return bird
        }

    protected open fun breed() {
        val mate = this.partner ?: return
        this.bird.spawnChildFromBreeding(this.level as ServerLevel, mate)
    }

    companion object {
        private val PARTNER_TARGETING: TargetingConditions =
            TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight()
    }
}