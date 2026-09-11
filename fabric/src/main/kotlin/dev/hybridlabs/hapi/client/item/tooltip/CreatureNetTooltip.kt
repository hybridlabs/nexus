package dev.hybridlabs.hapi.client.item.tooltip

import dev.hybridlabs.hapi.item.CreatureNetItem
import dev.hybridlabs.hapi.item.HAPIItems
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag


class CreatureNetTooltip :

    PredicateItemTooltipCallback(HAPIItems.CREATURE_NET.get()) {
    override fun appendTooltip(stack: ItemStack, context: TooltipFlag, lines: MutableList<Component>) {
        val nbtCopy = stack.components[DataComponents.CUSTOM_DATA]
        if (nbtCopy != null) {
            val optionalEntity = CreatureNetItem.getEntityFromNBT(nbtCopy)
            if (optionalEntity.isPresent) {
                val entityName = optionalEntity.get().description
                lines.add(Component.translatable("item.hapi.creature_net.description", entityName))
            }
        }
    }
}