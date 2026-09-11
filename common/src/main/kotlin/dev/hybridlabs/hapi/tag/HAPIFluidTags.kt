package dev.hybridlabs.hapi.tag

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import dev.hybridlabs.hapi.CommonClass

object HAPIFluidTags {
    val BRINE = create("brine")

    private fun create(id: String): TagKey<Fluid> {
        return TagKey.create(Registries.FLUID, CommonClass.locate(id))
    }
}
