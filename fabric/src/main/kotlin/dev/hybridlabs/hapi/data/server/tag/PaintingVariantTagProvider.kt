package dev.hybridlabs.hapi.data.server.tag

import dev.hybridlabs.hapi.tag.HAPIPaintingTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.decoration.PaintingVariant
import java.util.concurrent.CompletableFuture

class PaintingVariantTagProvider (output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<PaintingVariant>(output, Registries.PAINTING_VARIANT, registriesFuture) {

    override fun addTags(arg: HolderLookup.Provider) {
        getOrCreateTagBuilder(HAPIPaintingTags.KEEPS_PAINTING_VARIANT)

        getOrCreateTagBuilder(HAPIPaintingTags.TRANSPARENT_PAINTING)
    }
}