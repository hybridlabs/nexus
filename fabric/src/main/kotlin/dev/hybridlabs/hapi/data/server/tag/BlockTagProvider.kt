package dev.hybridlabs.hapi.data.server.tag

import dev.hybridlabs.hapi.tag.HAPIBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.CompletableFuture

class BlockTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun addTags(arg: HolderLookup.Provider) {

        getOrCreateTagBuilder(HAPIBlockTags.DRIFTWOOD_LOG)
            .addOptional(ResourceLocation.fromNamespaceAndPath("hybrid_aquatic", "driftwood_log"))

        getOrCreateTagBuilder(HAPIBlockTags.DRIFTWOOD_PLANKS)
            .addOptional(ResourceLocation.fromNamespaceAndPath("hybrid_aquatic", "driftwood_planks"))

        getOrCreateTagBuilder(HAPIBlockTags.DRIFTWOOD_SLAB)
            .addOptional(ResourceLocation.fromNamespaceAndPath("hybrid_aquatic", "driftwood_slab"))
    }
}
