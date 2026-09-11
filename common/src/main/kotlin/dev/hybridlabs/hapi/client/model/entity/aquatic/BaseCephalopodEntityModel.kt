package dev.hybridlabs.hapi.client.model.entity.aquatic

import dev.hybridlabs.hapi.entity.base.aquatic.BaseCephalopodEntity
import net.minecraft.client.model.geom.PartNames
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseCephalopodEntityModel<T : BaseCephalopodEntity>(
    private val namespace: String,
    private val id: String
) : GeoModel<T>() {

    override fun getModelResource(animatable: T): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            namespace,
            "geo/cephalopod/$id/$id.geo.json"
        )
    }

    override fun getTextureResource(animatable: T): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            namespace,
            "textures/entity/cephalopod/$id/$id.png"
        )
    }

    override fun getAnimationResource(animatable: T): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            namespace,
            "animations/entity/cephalopod/$id/$id.animation.json"
        )
    }

    override fun setCustomAnimations(
        animatable: T,
        instanceId: Long,
        animationState: AnimationState<T>
    ) {
        super.setCustomAnimations(animatable, instanceId, animationState)
        val deltaTime: Float = animationState.partialTick

        val body = animationProcessor.getBone(PartNames.BODY)
        body.rotX = Mth.lerp(deltaTime, animatable.xRot, animatable.xRotO) * -Mth.DEG_TO_RAD
    }
}