package dev.hybridlabs.hapi.client.model.entity.aquatic

import dev.hybridlabs.hapi.entity.base.aquatic.BaseJellyfishEntity
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseJellyfishEntityModel<T : BaseJellyfishEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {
    
    override fun getModelResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "geo/jellyfish/$id/$id.geo.json")
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "textures/entity/jellyfish/$id/$id.png")
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return ResourceLocation(
            namespace,
            "animations/entity/jellyfish/$id/$id.animation.json")
    }
}