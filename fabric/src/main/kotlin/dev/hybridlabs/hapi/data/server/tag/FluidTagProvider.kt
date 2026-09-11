package dev.hybridlabs.hapi.data.server.tag

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class FluidTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>):
    FabricTagProvider.FluidTagProvider(output, registriesFuture) {

    override fun addTags(p0: HolderLookup.Provider) {
    }
}