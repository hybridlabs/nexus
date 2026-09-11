package dev.hybridlabs.hapi.client.model.entity.aquatic

import dev.hybridlabs.hapi.entity.base.aquatic.BaseCrustaceanEntity
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseCrustaceanEntityModel<T : BaseCrustaceanEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {

    override fun getModelResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "geo/crustacean/$id/$id.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "textures/entity/crustacean/$id/$id.png")
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "animations/entity/crustacean/$id/$id.animation.json")
    }

    fun getLayerTextureResource(layer: String): ResourceLocation {
        return ResourceLocation(
            namespace,
            "textures/entity/crustacean/$id/layers/${id}_$layer.png")
    }
}
