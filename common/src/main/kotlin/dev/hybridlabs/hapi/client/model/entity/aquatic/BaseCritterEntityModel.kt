package dev.hybridlabs.hapi.client.model.entity.aquatic

import dev.hybridlabs.hapi.entity.base.aquatic.BaseCritterEntity
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseCritterEntityModel<T : BaseCritterEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {

    override fun getModelResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "geo/critter/$id/$id.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "textures/entity/critter/$id/$id.png")
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "animations/entity/critter/$id/$id.animation.json")
    }

    open fun getLayerTextureResource(layer: String): ResourceLocation {
        return ResourceLocation(
            namespace,
            "textures/entity/critter/$id/layers/${id}_$layer.png")
    }
}