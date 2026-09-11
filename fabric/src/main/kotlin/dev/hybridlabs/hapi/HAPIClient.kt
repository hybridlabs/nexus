@file:Suppress("UNUSED_PARAMETER")

package dev.hybridlabs.hapi

import dev.hybridlabs.hapi.client.item.tooltip.CreatureNetTooltip
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback

@Suppress("UnusedExpression", "DEPRECATION")
object HAPIClient : ClientModInitializer {
    override fun onInitializeClient() {

    }

    private fun registerTooltips() {
        ItemTooltipCallback.EVENT.register(CreatureNetTooltip())
    }
}