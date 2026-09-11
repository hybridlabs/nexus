package dev.hybridlabs.hapi;

import dev.hybridlabs.hapi.platform.Services;
import dev.hybridlabs.hapi.platform.registration.RegistrationProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static dev.hybridlabs.hapi.Constants.MOD_ID;

// This class is part of the common project meaning it is shared between all supported loaders. Code
//  written here can only  import and access the vanilla codebase, libraries used by vanilla, and
// optionally third party  libraries that provide  common compatible binaries. This means common
// code can not directly use loader specific concepts  such as Forge events however it will be
// compatible with all supported mod loaders.
public class CommonClass {

    public static final RegistrationProvider<Item> ITEMS =
            RegistrationProvider.get(BuiltInRegistries.ITEM, MOD_ID);

    public static void init() {
        Constants.LOGGER.info(
                "Hello from Common init on {}! we are currently in a {} environment!",
                Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName());

        if (Services.PLATFORM.isModLoaded(MOD_ID)) {
            Constants.LOGGER.info("Hybrid API loaded.");
        }
    }

    public static ResourceLocation locate(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
