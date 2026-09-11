package dev.hybridlabs.hapi.data.server.tag

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Instrument
import java.util.concurrent.CompletableFuture

class InstrumentTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Instrument>(output, Registries.INSTRUMENT ,registriesFuture) {

    override fun addTags(arg: HolderLookup.Provider) {
    }
}