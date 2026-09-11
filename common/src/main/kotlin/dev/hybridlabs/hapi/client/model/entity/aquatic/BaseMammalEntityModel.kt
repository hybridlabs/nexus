package dev.hybridlabs.hapi.client.model.entity.aquatic

import dev.hybridlabs.hapi.entity.base.aquatic.BaseMammalEntity
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseMammalEntityModel<T : BaseMammalEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {

    override fun getModelResource(animatable: T): ResourceLocation {
        return if (animatable.isBaby) {
            ResourceLocation.fromNamespaceAndPath(
            namespace,
            "geo/mammal/$id/baby_$id.geo.json")
        } else {
            ResourceLocation.fromNamespaceAndPath(
            namespace,
            "geo/mammal/$id/$id.geo.json")
        }
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return if (animatable.isBaby) {
            ResourceLocation.fromNamespaceAndPath(
            namespace,
            "textures/entity/mammal/$id/baby_$id.png")
        } else {
            ResourceLocation.fromNamespaceAndPath(
            namespace,
            "textures/entity/mammal/$id/$id.png")
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            namespace,
            "animations/entity/mammal/$id/$id.animation.json")
    }

    fun getLayerTextureResource(layer: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            namespace,
            "textures/entity/mammal/$id/layers/${id}_$layer.png")
    }

    override fun setCustomAnimations(
        animatable: T,
        instanceId: Long,
        animationState: AnimationState<T>,
    ) {
        val head = animationProcessor.getBone("head")

        if (head != null) {
            val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA)

            head.rotX = entityData!!.headPitch() * Mth.DEG_TO_RAD
            head.rotY = entityData.netHeadYaw() * Mth.DEG_TO_RAD
        }
    }
}