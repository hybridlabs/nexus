package dev.hybridlabs.hapi.item

import dev.hybridlabs.hapi.CommonClass
import net.minecraft.world.item.Item
import java.util.function.Supplier

object HAPIItems {

    val CREATURE_NET = register("creature_net") { CreatureNetItem(Item.Properties().stacksTo(1)) }

    fun register(id: String, item: Supplier<Item>): Supplier<Item> {
        return CommonClass.ITEMS.register(id, item)
    }
}