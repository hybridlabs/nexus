package dev.hybridlabs.hapi.data

import dev.hybridlabs.hapi.data.server.tag.BiomeTagProvider
import dev.hybridlabs.hapi.data.server.tag.BlockTagProvider
import dev.hybridlabs.hapi.data.server.tag.EntityTypeTagProvider
import dev.hybridlabs.hapi.data.server.tag.FluidTagProvider
import dev.hybridlabs.hapi.data.server.tag.InstrumentTagProvider
import dev.hybridlabs.hapi.data.server.tag.ItemTagProvider
import dev.hybridlabs.hapi.data.server.tag.PaintingVariantTagProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.Registry
import net.minecraft.core.RegistrySetBuilder
import dev.hybridlabs.hapi.Constants
import dev.hybridlabs.hapi.data.client.LanguageProvider
import dev.hybridlabs.hapi.data.server.RecipeProvider

object HAPIDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()
        pack.addProvider(::BiomeTagProvider)
        pack.addProvider(::BlockTagProvider)
        pack.addProvider(::PaintingVariantTagProvider)
        pack.addProvider(::ItemTagProvider)
        pack.addProvider(::InstrumentTagProvider)
        pack.addProvider(::EntityTypeTagProvider)
        pack.addProvider(::FluidTagProvider)
        pack.addProvider(::LanguageProvider)
        pack.addProvider(::RecipeProvider)
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {

        fun <T> filterHybridLabs(registry: Registry<T>): (T & Any) -> Boolean {
            return { o ->
                val id = registry.getKey(o)
                id!!.namespace == Constants.MOD_ID
            }
        }
    }
}
