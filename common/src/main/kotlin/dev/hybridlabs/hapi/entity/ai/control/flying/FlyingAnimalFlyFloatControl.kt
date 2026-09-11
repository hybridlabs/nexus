package dev.hybridlabs.hapi.entity.ai.control.flying

import dev.hybridlabs.hapi.entity.base.flying.BaseFlyingAnimal
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import kotlin.math.max

// credit to fowl play for the code

class FlyingAnimalFlyFloatControl(
    bird: BaseFlyingAnimal,
    maxTurn: Int,
    hoversInPlace: Boolean
) : FlyingMoveControl(bird, maxTurn, hoversInPlace) {

    override fun tick() {
        val bird = this.mob as BaseFlyingAnimal

        if (bird.isBelowWaterline()) {
            var delta = bird.deltaMovement

            delta = delta.add(0.0, 0.05, 0.0)

            if (bird.isUnderWater) {
                delta = delta.add(0.0, 0.05, 0.0)
            }

            bird.setDeltaMovement(
                delta.x,
                max(delta.y, 0.0),
                delta.z
            )
        }
        super.tick()
    }
}
