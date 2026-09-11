package dev.hybridlabs.hapi.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import kotlin.let

class ProgressiveDrinkItem(
    properties: Properties,
    private val nextItem: () -> Item?
) : Item(properties) {

    override fun finishUsingItem(
        stack: ItemStack,
        level: Level,
        entityLiving: LivingEntity
    ): ItemStack {
        super.finishUsingItem(stack, level, entityLiving)

        if (entityLiving is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(entityLiving, stack)
            entityLiving.awardStat(Stats.ITEM_USED.get(this))
        }

        if (entityLiving is Player && !entityLiving.abilities.instabuild) {
            stack.shrink(1)
        }

        return nextItem()?.let(::ItemStack) ?: ItemStack.EMPTY
    }

    override fun getUseDuration(stack: ItemStack): Int = 32

    override fun getDrinkingSound(): SoundEvent {
        return SoundEvents.GENERIC_DRINK
    }

    override fun getEatingSound(): SoundEvent {
        return SoundEvents.GENERIC_DRINK
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun use(
        level: Level,
        player: Player,
        hand: InteractionHand
    ): InteractionResultHolder<ItemStack?> {
        return ItemUtils.startUsingInstantly(level, player, hand)
    }
}