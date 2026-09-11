package dev.hybridlabs.hapi.data.client

import dev.hybridlabs.hapi.item.HAPIItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture

class LanguageProvider( output: FabricDataOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : FabricLanguageProvider(output,lookupProvider) {
    override fun generateTranslations(lookupProvider: HolderLookup.Provider, builder: TranslationBuilder) {
        // item group
        mapOf(
            HAPIItems.CREATURE_NET.get() to "Creature Net",
        ).forEach { (item, translation) ->
            builder.add(item, translation)
        }

        mapOf(
            HAPIItems.CREATURE_NET.get().descriptionId to "Stored Entity: %s",
        ).forEach { (itemTranslationKey, translation) ->
            builder.add(itemTranslationKey.plus(".description"), translation)
        }

        // Item Functions
        mapOf(
            HAPIItems.CREATURE_NET.get().descriptionId to "Lets you catch and move aquatic creatures",
        ).forEach { (itemTranslationKey, translation) ->
            builder.add(itemTranslationKey.plus(".function"), translation)
        }

        mapOf(
            HAPIItems.CREATURE_NET.get().descriptionId to "Placed creatures become passive and don't despawn",
        ).forEach { (itemTranslationKey, translation) ->
            builder.add(itemTranslationKey.plus(".properties"), translation)
        }
    }
}
