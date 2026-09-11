package dev.hybridlabs.birds.entity.ai.goal

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.world.entity.ai.goal.Goal

class FlyingAnimalWalkGoal(
    private val bird: BaseFlyingAnimal,
) : Goal() {

    override fun canUse(): Boolean {
        TODO("Not yet implemented")
    }
}