package dev.hybridlabs.hapi.client.render.entity.flying

import dev.hybridlabs.hapi.entity.base.flying.BaseParrotEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer

open class BaseParrotEntityRenderer<T : BaseParrotEntity>(
    context: EntityRendererProvider.Context,
    model: GeoModel<T>
) : GeoEntityRenderer<T>(context, model) {
    init {
        this.shadowRadius = 0.3f
    }
}
