package com.robotemployee.foliant;

import com.mojang.logging.LogUtils;
import com.robotemployee.foliant.registry.*;
import com.robotemployee.reu.util.datagen.DatagenInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Foliant.MODID)
public class Foliant {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "foliant";
    public static final DatagenInstance DATAGEN = new DatagenInstance(MODID);

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "foliant" namespace

    public Foliant(@NotNull FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModEntityDataSerializers.SERIALIZERS.register(modEventBus);
        ModMobEffects.EFFECTS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        // the rest of these don't care as much about the ordering
        ModAdvancements.register();
        ModDamageTypes.idk();

        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // same for eeeverything else that wants to hook into events
        MinecraftForge.EVENT_BUS.register(FoliantEvents.class);

        // only for the client :)
        /*
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ClientModEvents::onClientSetup);
            MinecraftForge.EVENT_BUS.register(ClientModEvents.class);
        });
         */


        //MinecraftForge.EVENT_BUS.register(Datagen.class);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DATAGEN.run(event);
    }

    @Mod.EventBusSubscriber(modid = Foliant.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onGatherData(GatherDataEvent event) {
            DATAGEN.run(event);
        }
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        private static final ArrayList<Consumer<FMLClientSetupEvent>> CLIENT_SETUP_REQUESTS = new ArrayList<>();

        public static <T extends Entity> void addCustomRenderer(Supplier<EntityType<? extends T>> entity, EntityRendererProvider<T> renderer) {
            CLIENT_SETUP_REQUESTS.add(fmlClientSetupEvent -> {
                EntityRenderers.register(entity.get(), renderer);
            });
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CLIENT_SETUP_REQUESTS.forEach(request -> request.accept(event));
        }
    }

}
