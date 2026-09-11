package dev.hybridlabs.hapi.tag

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import dev.hybridlabs.hapi.CommonClass

object HAPIBlockTags {

    val PLUSHIES = create("plushies")
    val DRIFTWOOD_LOG = create("driftwood_log")
    val DRIFTWOOD_PLANKS = create("driftwood_planks")
    val DRIFTWOOD_SLAB = create("driftwood_slab")

    private fun create(id: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, CommonClass.locate(id))
    }
}
