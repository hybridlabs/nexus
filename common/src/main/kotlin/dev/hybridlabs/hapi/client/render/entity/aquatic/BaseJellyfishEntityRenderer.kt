package dev.hybridlabs.hapi.client.render.entity.aquatic

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.hybridlabs.hapi.entity.base.aquatic.BaseJellyfishEntity
import dev.hybridlabs.hapi.entity.base.aquatic.BaseWaterAnimal
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.util.Mth
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

@Suppress("LeakingThis")
open class BaseJellyfishEntityRenderer<T : BaseJellyfishEntity>(
    context: EntityRendererProvider.Context,
    model: GeoModel<T>,
    private var variableSize: Boolean = false,
    canGlow: Boolean = false
) : GeoEntityRenderer<T>(context, model) {

    init {
        if (canGlow) addRenderLayer(AutoGlowingGeoLayer(this))
    }

    override fun applyRotations(
        jellyfishEntity: T,
        poseStack: PoseStack,
        ageInTicks: Float,
        rotationYaw: Float,
        partialTick: Float
    ) {
        val i = Mth.lerp(partialTick, jellyfishEntity.prevTiltAngle, jellyfishEntity.tiltAngle)
        poseStack.translate(0.0f, 0.25f, 0.0f)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - rotationYaw))
        poseStack.mulPose(Axis.XP.rotationDegrees(i))
        poseStack.translate(0.0f, 0.0f, 0.0f)
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
            val size = BaseWaterAnimal.Companion.getScaleAdjustment(entity, 0.05f)
            poseStack.scale(size, size, size)
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }
}