package dev.hybridlabs.hapi.data.server.tag

import dev.hybridlabs.hapi.tag.HAPIBiomeTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import java.util.concurrent.CompletableFuture

class BiomeTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(output, Registries.BIOME, registriesFuture) {
    override fun addTags(arg: HolderLookup.Provider) {

        //#region Vanilla Tags
        getOrCreateTagBuilder(BiomeTags.HAS_RUINED_PORTAL_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "seagrass_bed"))
            .addOptional(ResourceLocation("hybrid_aquatic", "red_meadow"))
            .addOptional(ResourceLocation("hybrid_aquatic", "coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "lukewarm_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "warm_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "cold_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "frozen_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_deep_coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_coral_reef"))

        getOrCreateTagBuilder(BiomeTags.HAS_CLOSER_WATER_FOG)
            .forceAddTag(BiomeTags.IS_DEEP_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "sulfuric_caves"))
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(BiomeTags.IS_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "seagrass_bed"))
            .addOptional(ResourceLocation("hybrid_aquatic", "red_meadow"))
            .addOptional(ResourceLocation("hybrid_aquatic", "coral_reef"))

        getOrCreateTagBuilder(BiomeTags.IS_DEEP_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_deep_coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_warm_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.HAPI_DEEP_OCEANS)
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_warm_ocean"))
            .addOptional(Biomes.DEEP_OCEAN)
            .addOptional(Biomes.DEEP_COLD_OCEAN)
            .addOptional(Biomes.DEEP_FROZEN_OCEAN)
            .addOptional(Biomes.DEEP_LUKEWARM_OCEAN)

        getOrCreateTagBuilder(HAPIBiomeTags.CAN_SUMMON_SHELL_BEAST)
            .addOptionalTag(HAPIBiomeTags.HAPI_DEEP_OCEANS)
            .addOptionalTag(HAPIBiomeTags.ALL_TRENCHES)
            .addTag(BiomeTags.IS_DEEP_OCEAN)

        getOrCreateTagBuilder(BiomeTags.IS_RIVER)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))
        //#endregion

        //#region Arctic Ocean Tags
        getOrCreateTagBuilder(HAPIBiomeTags.FROZEN_OCEANS)
            .add(
                Biomes.FROZEN_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN
            )
            .addOptional(ResourceLocation("still_life", "arctic_deep_ocean"))
            .addOptional(ResourceLocation("still_life", "arctic_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.SHALLOW_FROZEN_OCEANS)
            .add(Biomes.FROZEN_OCEAN)
            .addOptional(ResourceLocation("still_life", "arctic_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_FROZEN_OCEANS)
            .add(Biomes.DEEP_FROZEN_OCEAN)
            .addOptional(ResourceLocation("still_life", "arctic_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.FROZEN_TRENCH)
            .addOptional(ResourceLocation("hybrid_aquatic", "frozen_trench"))
        //#endregion

        //#region Cold Ocean Tags
        getOrCreateTagBuilder(HAPIBiomeTags.COLD_OCEANS)
            .add(
                Biomes.COLD_OCEAN,
                Biomes.DEEP_COLD_OCEAN
            )
            .addOptional(ResourceLocation("still_life", "cold_shallow_ocean"))
            .addOptional(ResourceLocation("still_life", "cold_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.SHALLOW_COLD_OCEANS)
            .add(Biomes.COLD_OCEAN)
            .addOptional(ResourceLocation("still_life", "cold_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_COLD_OCEANS)
            .add(Biomes.DEEP_COLD_OCEAN)
            .addOptional(ResourceLocation("still_life", "cold_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.COLD_TRENCH)
            .addOptional(ResourceLocation("hybrid_aquatic", "cold_trench"))
        //#endregion

        //#region Temperate Ocean Tags
        getOrCreateTagBuilder(HAPIBiomeTags.TEMPERATE_OCEANS)
            .add(
                Biomes.OCEAN,
                Biomes.DEEP_OCEAN
            )
            .addOptional(ResourceLocation("still_life", "temperate_shallow_ocean"))
            .addOptional(ResourceLocation("still_life", "temperate_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.SHALLOW_TEMPERATE_OCEANS)
            .add(Biomes.OCEAN)
            .addOptional(ResourceLocation("still_life", "temperate_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_TEMPERATE_OCEANS)
            .add(Biomes.DEEP_OCEAN)
            .addOptional(ResourceLocation("still_life", "temperate_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.TEMPERATE_TRENCH)
            .addOptional(ResourceLocation("hybrid_aquatic", "trench"))
        //#endregion

        //#region Lukewarm Ocean Tags
        getOrCreateTagBuilder(HAPIBiomeTags.SHALLOW_LUKEWARM_OCEANS)
            .add(Biomes.LUKEWARM_OCEAN)
            .addOptional(ResourceLocation("still_life", "subtropical_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.LUKEWARM_OCEANS)
            .add(
                Biomes.LUKEWARM_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN
            )
            .addOptional(ResourceLocation("still_life", "subtropical_shallow_ocean"))
            .addOptional(ResourceLocation("still_life", "subtropical_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_LUKEWARM_OCEANS)
            .add(Biomes.DEEP_LUKEWARM_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_deep_coral_reef"))
            .addOptional(ResourceLocation("still_life", "subtropical_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.LUKEWARM_TRENCH)
            .addOptional(ResourceLocation("hybrid_aquatic", "lukewarm_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "warm_trench"))
        //#endregion

        //#region Warm Ocean Tags
        getOrCreateTagBuilder(HAPIBiomeTags.SHALLOW_WARM_OCEANS)
            .add(Biomes.WARM_OCEAN)
            .addOptional(ResourceLocation("still_life", "tropical_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.WARM_OCEANS)
            .add(Biomes.WARM_OCEAN)
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_warm_ocean"))
            .addOptional(ResourceLocation("still_life", "tropical_deep_ocean"))
            .addOptional(ResourceLocation("still_life", "tropical_shallow_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_WARM_OCEANS)
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_warm_ocean"))
            .addOptional(ResourceLocation("spawn", "deep_warm_ocean"))
            .addOptional(ResourceLocation("still_life", "tropical_deep_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.WARM_TRENCH)
            .addOptional(ResourceLocation("hybrid_aquatic", "warm_trench"))

        getOrCreateTagBuilder(HAPIBiomeTags.CORAL_REEF)
            .addOptional(ResourceLocation("hybrid_aquatic", "coral_reef"))
            .addOptional(ResourceLocation("regions_unexplored", "rocky_reef"))
            .addOptional(ResourceLocation("biomeswevegone", "lush_stacks"))

        getOrCreateTagBuilder(HAPIBiomeTags.SEAGRASS_BED)
            .addOptional(ResourceLocation("hybrid_aquatic", "seagrass_bed"))
            .addOptional(ResourceLocation("spawn", "seagrass_meadow"))

        getOrCreateTagBuilder(HAPIBiomeTags.RED_MEADOW)
            .addOptional(ResourceLocation("hybrid_aquatic", "red_meadow"))
        //#endregion

        //#region Misc Deep Sea Tags
        getOrCreateTagBuilder(HAPIBiomeTags.ALL_TRENCHES)
            .addOptional(ResourceLocation("hybrid_aquatic", "frozen_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "cold_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "lukewarm_trench"))
            .addOptional(ResourceLocation("hybrid_aquatic", "warm_trench"))

        getOrCreateTagBuilder(HAPIBiomeTags.DEEP_REEF)
            .addOptional(ResourceLocation("hybrid_aquatic", "deep_coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_deep_coral_reef"))
            .addOptional(ResourceLocation("spawn", "deep_warm_ocean"))

        getOrCreateTagBuilder(HAPIBiomeTags.HAS_WHALE_FALL)
            .addTag(HAPIBiomeTags.ALL_TRENCHES)
            .addTag(BiomeTags.IS_DEEP_OCEAN)

        getOrCreateTagBuilder(HAPIBiomeTags.HAS_THERMAL_VENTS)
            .addOptional(ResourceLocation("hybrid_aquatic", "sulfuric_caves"))

        getOrCreateTagBuilder(HAPIBiomeTags.SULFURIC_CAVE)
            .addOptional(ResourceLocation("hybrid_aquatic", "sulfuric_caves"))
        //#endregion

        //#region Beach Tags
        getOrCreateTagBuilder(HAPIBiomeTags.SANDY_BEACHES)
            .add(Biomes.BEACH)
            .addOptional(ResourceLocation("wythers", "tropical_beach"))
            .addOptional(ResourceLocation("biomesoplenty", "dune_beach"))
            .addOptional(ResourceLocation("biomeswevegone", "rainbow_beach"))
            .addOptional(ResourceLocation("terrestria", "volcanic_island_beach"))
            .addOptional(ResourceLocation("mysticsbiomes", "lagoon"))
            .addOptional(ResourceLocation("still_life", "temperate_beach"))
            .addOptional(ResourceLocation("still_life", "mediterranean_beach"))
            .addOptional(ResourceLocation("still_life", "arid_beach"))

        getOrCreateTagBuilder(HAPIBiomeTags.ROCKY_BEACHES)
            .add(Biomes.STONY_SHORE)
            .addOptional(ResourceLocation("biomesoplenty", "gravel_beach"))
            .addOptional(ResourceLocation("biomeswevegone", "dacite_shore"))
            .addOptional(ResourceLocation("biomeswevegone", "basalt_barrera"))
            .addOptional(ResourceLocation("still_life", "taiga_beach"))
            .addOptional(ResourceLocation("still_life", "tundra_beach"))
            .addOptional(ResourceLocation("still_life", "arctic_beach"))

        getOrCreateTagBuilder(HAPIBiomeTags.TIDE_POOLS)
            .addOptional(ResourceLocation("hybrid_aquatic", "tide_pools"))
        //#endregion

        //#region River Tags
        getOrCreateTagBuilder(HAPIBiomeTags.RIVERS)
            .add(Biomes.RIVER)
            .addOptional(ResourceLocation("regions_unexplored", "muddy_river"))
            .addOptional(ResourceLocation("riverredux", "sandy_river"))
            .addOptional(ResourceLocation("riverredux", "carved_river"))
            .addOptional(ResourceLocation("still_life", "temperate_river"))
            .addOptional(ResourceLocation("still_life", "warm_temperate_river"))
            .addOptional(ResourceLocation("still_life", "mediterranean_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.TROPICAL_RIVERS)
            .addOptional(ResourceLocation("wythers", "jungle_river"))
            .addOptional(ResourceLocation("wythers", "tropical_forest_river"))
            .addOptional(ResourceLocation("terralith", "warm_river"))
            .addOptional(ResourceLocation("regions_unexplored", "tropical_river"))
            .addOptional(ResourceLocation("riverredux", "tropical_river"))
            .addOptional(ResourceLocation("still_life", "steppe_river"))
            .addOptional(ResourceLocation("still_life", "tropical_rainforest_river"))
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.COLD_RIVERS)
            .add(Biomes.FROZEN_RIVER)
            .addOptional(ResourceLocation("riverredux", "gravelly_river"))
            .addOptional(ResourceLocation("regions_unexplored", "cold_river"))
            .addOptional(ResourceLocation("still_life", "arctic_river"))
            .addOptional(ResourceLocation("still_life", "tundra_river"))
            .addOptional(ResourceLocation("still_life", "boreal_river"))
        //#endregion

        //#region Misc Biome Tags
        getOrCreateTagBuilder(HAPIBiomeTags.JUNGLE)
            .forceAddTag(BiomeTags.IS_JUNGLE)
            .addOptional(ResourceLocation("regions_unexplored", "eucalyptus_forest"))
            .addOptional(ResourceLocation("regions_unexplored", "rainforest"))
            .addOptional(ResourceLocation("regions_unexplored", "sparse_rainforest"))
            .addOptional(ResourceLocation("wythers", "flooded_jungle"))
            .addOptional(ResourceLocation("biomesoplenty", "rainforest"))
            .addOptional(ResourceLocation("biomesoplenty", "rocky_rainforest"))
            .addOptional(ResourceLocation("biomeswevegone", "crag_gardens"))
            .addOptional(ResourceLocation("biomeswevegone", "jacaranda_jungle"))
            .addOptional(ResourceLocation("biomeswevegone", "fragment_jungle"))
            .addOptional(ResourceLocation("biomeswevegone", "tropical_rainforest"))
            .addOptional(ResourceLocation("terrestria", "hemlock_rainforest"))
            .addOptional(ResourceLocation("terrestria", "hemlock_clearing"))
            .addOptional(ResourceLocation("terrestria", "rainbow_rainforest"))
            .addOptional(ResourceLocation("terrestria", "rainbow_rainforest_lake"))

        getOrCreateTagBuilder(HAPIBiomeTags.CHERRY)
            .add(Biomes.CHERRY_GROVE)
            .addOptional(ResourceLocation("regions_unexplored", "mauve_hills"))
            .addOptional(ResourceLocation("regions_unexplored", "magnolia_woodland"))

        getOrCreateTagBuilder(HAPIBiomeTags.CAVES)
            .add(Biomes.LUSH_CAVES)
            .add(Biomes.DRIPSTONE_CAVES)
            .add(Biomes.DEEP_DARK)
            .addOptional(ResourceLocation("regions_unexplored", "redstone_caves"))
            .addOptional(ResourceLocation("regions_unexplored", "bioshroom_caves"))
            .addOptional(ResourceLocation("regions_unexplored", "scorching_caves"))
            .addOptional(ResourceLocation("regions_unexplored", "ancient_delta"))
            .addOptional(ResourceLocation("regions_unexplored", "prismachasm"))

        getOrCreateTagBuilder(HAPIBiomeTags.SWAMP)
            .add(Biomes.SWAMP)
            .addOptional(ResourceLocation("regions_unexplored", "bayou"))
            .addOptional(ResourceLocation("wythers", "waterlily_swamp"))
            .addOptional(ResourceLocation("terralith", "orchid_swamp"))
            .addOptional(ResourceLocation("biomesoplenty", "bayou"))
            .addOptional(ResourceLocation("biomeswevegone", "cypress_swamplands"))
            .addOptional(ResourceLocation("biomeswevegone", "bayou"))
            .addOptional(ResourceLocation("terrestria", "cypress_swamp"))
            .addOptional(ResourceLocation("still_life", "temperate_swamp"))

        getOrCreateTagBuilder(HAPIBiomeTags.MARSHES)
            .addOptional(ResourceLocation("regions_unexplored", "marsh"))
            .addOptional(ResourceLocation("biomesoplenty", "marsh"))
            .addOptional(ResourceLocation("biomesoplenty", "wetland"))
            .addOptional(ResourceLocation("biomesoplenty", "floodplain"))
            .addOptional(ResourceLocation("still_life", "bog"))
            .addOptional(ResourceLocation("still_life", "temperate_marsh"))
            .addOptional(ResourceLocation("still_life", "fen"))
            .addOptional(ResourceLocation("still_life", "mire"))
            .addOptional(ResourceLocation("still_life", "flooded_grasslands"))
            .addOptional(ResourceLocation("still_life", "mediterranean_marsh"))

        getOrCreateTagBuilder(HAPIBiomeTags.MANGROVES)
            .add(Biomes.MANGROVE_SWAMP)
            .addOptional(ResourceLocation("biomeswevegone", "white_mangrove_marshes"))
            .addOptional(ResourceLocation("still_life", "mangrove_marsh"))
        //#endregion

        //#region Misc Tags
        getOrCreateTagBuilder(HAPIBiomeTags.BOTTLE_SPAWN_BIOMES)
            .forceAddTag(BiomeTags.IS_OCEAN)
            .forceAddTag(BiomeTags.IS_BEACH)
        //#endregion

        //#region Compatibility Tags
            // rainbow reef
        getOrCreateTagBuilder(HAPIBiomeTags.RR_WARM_OCEANS)
            .addOptional(ResourceLocation("hybrid_aquatic", "coral_reef"))

            // fintastic
        getOrCreateTagBuilder(HAPIBiomeTags.MOONY_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "coral_reef"))
            .addOptional(ResourceLocation("hybrid_aquatic", "tide_pools"))

        getOrCreateTagBuilder(HAPIBiomeTags.PLECO_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.ARAPAIMA_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.CATFISH_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.GUPPY_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.MINNOW_SURFACE_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))

        getOrCreateTagBuilder(HAPIBiomeTags.FWSHARK_BIOMES)
            .addOptional(ResourceLocation("hybrid_aquatic", "tropical_river"))
        //#endregion
    }
}