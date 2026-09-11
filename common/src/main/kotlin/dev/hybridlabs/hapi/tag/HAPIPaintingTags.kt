package dev.hybridlabs.hapi.tag

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.decoration.PaintingVariant
import dev.hybridlabs.hapi.CommonClass

object HAPIPaintingTags {
    val KEEPS_PAINTING_VARIANT = create("keeps_painting_variant")
    val TRANSPARENT_PAINTING = create("transparent_painting")

    private fun create(id: String): TagKey<PaintingVariant> {
        return TagKey.create(Registries.PAINTING_VARIANT, CommonClass.locate(id))
    }
}