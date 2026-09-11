package dev.hybridlabs.birds.entity.ai.goal

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal

class FlyingAnimalRandomFlyingGoal(
    private val bird: BaseFlyingAnimal
    ) : WaterAvoidingRandomFlyingGoal(
    bird,
        1.0
    ) {

    override fun canUse(): Boolean {
        if (bird.isClipped) return false
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        if (bird.isClipped) return false
        return super.canContinueToUse()
    }
}