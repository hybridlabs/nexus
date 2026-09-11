package dev.hybridlabs.hapi.platform.services;

import dev.hybridlabs.hapi.Constants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLLoader;
import thedarkcolour.kotlinforforge.KotlinModContainer;

public class ForgePlatformHelper implements PlatformHelper {

    public static IEventBus getEventBus() {
        final ModContainer cont =
                ModList.get().getModContainerById(Constants.MOD_ID).orElseThrow();
        if (cont instanceof FMLModContainer fmlModContainer) {
            return fmlModContainer.getEventBus();
        } else if (cont instanceof KotlinModContainer kotlinModContainer) {
            return kotlinModContainer.getEventBus$kfflang();
        } else {
            throw new ClassCastException(
                    "The container of the mod " + Constants.MOD_ID + " is not a FML one!");
        }
    }

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
}
