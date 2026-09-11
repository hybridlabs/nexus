package dev.hybridlabs.hapi.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import kotlin.let

class ProgressiveFoodItem(
    properties: Properties,
    private val nextItem: () -> Item
) : Item(properties) {

    override fun finishUsingItem(stack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        // eating already awards stats, triggers advancements and shrinks the stack
        val result = super.finishUsingItem(stack, level, entity)

        if (entity is Player && entity.abilities.instabuild) {
            return result
        }

        val remainder = ItemStack(nextItem())
        if (result.isEmpty) {
            return remainder
        }

        if (entity is Player && !entity.inventory.add(remainder)) {
            entity.drop(remainder, false)
        }

        return result
    }

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun use(
        level: Level,
        player: Player,
        hand: InteractionHand
    ): InteractionResultHolder<ItemStack?> {
        return ItemUtils.startUsingInstantly(level, player, hand)
    }
}
