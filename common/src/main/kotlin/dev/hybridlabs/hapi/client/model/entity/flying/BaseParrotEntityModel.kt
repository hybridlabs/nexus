package dev.hybridlabs.hapi.client.model.entity.flying

import dev.hybridlabs.hapi.entity.base.flying.BaseParrotEntity
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseParrotEntityModel<T: BaseParrotEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {

    override fun getModelResource(animatable: T): ResourceLocation {
        return if (animatable.isBaby) {
            ResourceLocation(
                namespace,
                "geo/entity/$id/baby_$id.geo.json")
        } else {
            ResourceLocation(
                namespace,
                "geo/entity/$id/$id.geo.json")
        }
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return if (animatable.isBaby) {
            ResourceLocation(
                namespace,
                "textures/entity/$id/baby_$id.png")
        } else {
            ResourceLocation(
                namespace,
                "textures/entity/$id/$id.png")
        }
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return if (animatable.isBaby) {
            ResourceLocation(
                namespace,
                "animations/entity/$id/baby_$id.animation.json")
        } else {
            ResourceLocation(
                namespace,
                "animations/entity/$id/$id.animation.json")
        }
    }

    override fun setCustomAnimations(animatable: T, instanceId: Long, animationState: AnimationState<T>) {
        val head = animationProcessor.getBone("head")

        if (head != null) {
            val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA)

            head.rotX = entityData!!.headPitch() * Mth.DEG_TO_RAD
            head.rotY = entityData.netHeadYaw() * Mth.DEG_TO_RAD
        }
    }
}