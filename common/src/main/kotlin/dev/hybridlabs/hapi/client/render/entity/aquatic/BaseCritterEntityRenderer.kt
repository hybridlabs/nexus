package dev.hybridlabs.hapi.client.render.entity.aquatic

import com.mojang.blaze3d.vertex.PoseStack
import dev.hybridlabs.hapi.entity.base.aquatic.BaseCritterEntity
import dev.hybridlabs.hapi.entity.base.aquatic.BaseWaterAnimal
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer

open class BaseCritterEntityRenderer<T : BaseCritterEntity>(
    context: EntityRendererProvider.Context,
    model: GeoModel<T>,
    private var variableSize: Boolean = false
) : GeoEntityRenderer<T>(context, model) {

    override fun getMotionAnimThreshold(animatable: T): Float {
        return 0.0025f
    }

    override fun getDeathMaxRotation(animatable: T): Float {
        return 0f
    }

    override fun render(
        entity: T,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int
    ) {
        if (variableSize) {
            val size = BaseWaterAnimal.getScaleAdjustment(entity, 0.05f)
            poseStack.scale(size, size, size)
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }
}