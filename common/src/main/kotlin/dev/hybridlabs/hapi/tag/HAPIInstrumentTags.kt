package dev.hybridlabs.hapi.tag

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Instrument
import dev.hybridlabs.hapi.CommonClass

object HAPIInstrumentTags {
    var OMINOUS_CONCH = create("ominous_conch")

    private fun create(id: String): TagKey<Instrument> {
        return TagKey.create(Registries.INSTRUMENT, CommonClass.locate(id))
    }
}