package dev.hybridlabs.hapi.data.server

import dev.hybridlabs.hapi.item.HAPIItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class RecipeProvider(output: FabricDataOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    FabricRecipeProvider(output, lookupProvider) {
    override fun buildRecipes(exporter: RecipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HAPIItems.CREATURE_NET.get())
            .pattern("  S")
            .pattern(" IS")
            .pattern("I  ")
            .define('I', Items.STICK)
            .define('S', Items.STRING)
            .unlockedBy("string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
            .save(exporter)
    }
}