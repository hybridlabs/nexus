package dev.hybridlabs.hapi.entity.base.aquatic

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes

data class InkConfiguration(
    val particle: ParticleOptions,
) {
    companion object {
        val DEFAULT = InkConfiguration(ParticleTypes.SQUID_INK)
        val GLOW = InkConfiguration(ParticleTypes.GLOW_SQUID_INK)
    }
}