package dev.hybridlabs.hapi

import dev.hybridlabs.hapi.item.HAPIItems
import dev.hybridlabs.hapi.tag.HAPIBiomeTags
import dev.hybridlabs.hapi.tag.HAPIBlockTags
import dev.hybridlabs.hapi.tag.HAPIEntityTags
import dev.hybridlabs.hapi.tag.HAPIInstrumentTags
import dev.hybridlabs.hapi.tag.HAPIItemTags
import dev.hybridlabs.hapi.tag.HAPIPaintingTags
import net.fabricmc.api.ModInitializer

object HybridAPI : ModInitializer {

    @Suppress("UnusedExpression")
    override fun onInitialize() {
        CommonClass.init()

        HAPIBiomeTags
        HAPIBlockTags
        HAPIEntityTags
        HAPIInstrumentTags
        HAPIItemTags
        HAPIPaintingTags

        HAPIItems
    }
}