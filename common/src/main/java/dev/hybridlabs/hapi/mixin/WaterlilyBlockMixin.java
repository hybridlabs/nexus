package dev.hybridlabs.hapi.mixin;

import dev.hybridlabs.hapi.tag.HAPIEntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla only destroys a lily pad when the entity inside it is a {@link Boat}. Modded boats that
 * do not extend {@link Boat} (such as the ones built on HAPI's own boat base class) would otherwise
 * sail straight through lily pads. Anything in the {@code hapi:boats} entity type tag is treated
 * like a boat here, so mods can opt their vehicles in without depending on HAPI's code.
 */
@Mixin(WaterlilyBlock.class)
public class WaterlilyBlockMixin {

    @Inject(method = "entityInside", at = @At("TAIL"))
    private void hapi$breakForModdedBoats(
            BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        // Vanilla boats were already handled by the method we are injecting into.
        if (entity instanceof Boat) return;
        if (!(level instanceof ServerLevel)) return;
        if (!entity.getType().is(HAPIEntityTags.INSTANCE.getBOATS())) return;

        level.destroyBlock(pos, true);
    }
}