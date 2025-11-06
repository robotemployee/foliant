package com.robotemployee.foliant.registry;

import com.mojang.logging.LogUtils;
import com.robotemployee.foliant.Foliant;
import com.robotemployee.foliant.entity.*;
import com.robotemployee.foliant.render.AmelieRenderer;
import com.robotemployee.foliant.render.AsteirtoRenderer;
import com.robotemployee.foliant.render.DevilRenderer;
import com.robotemployee.foliant.render.GregRenderer;
import com.robotemployee.reu.util.registry.builder.EntityBuilder;
import com.robotemployee.reu.util.registry.entry.EntityRegistryEntry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

// least favorite annotation
@Mod.EventBusSubscriber(modid = Foliant.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    static Logger LOGGER = LogUtils.getLogger();
    private static final ArrayList<Consumer<EntityAttributeCreationEvent>> attributeCreationRequests = new ArrayList<>();
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Foliant.MODID);

    public static final EntityBuilder.Manager MANAGER = new EntityBuilder.Manager(Foliant.DATAGEN, ENTITIES, ModItems.MANAGER);

    public static final int FOLIANT_EGG_COLOR = 0xE2EE1A;

    public static final EntityRegistryEntry<DevilEntity> DEVIL =
            MANAGER.<DevilEntity>createBuilder()
                    .withTypeSupplier(
                    () -> EntityType.Builder.of(DevilEntity::new, MobCategory.MONSTER)
                            .sized(0.5f, 0.5f))
                    .withName("devil")
                    .withAttributes(DevilEntity::createAttributes)
                    .customRenderer(DevilRenderer::new)
                    .eggColor(0x4CC9E1, FOLIANT_EGG_COLOR)
                    .build();

    public static final EntityRegistryEntry<GregEntity> GREG =
            MANAGER.<GregEntity>createBuilder()
                    .withTypeSupplier(
                    () -> EntityType.Builder.of(GregEntity::new, MobCategory.MONSTER)
                            .sized(1, 0.65f))
                    .withName("greg")
                    .withAttributes(GregEntity::createAttributes)
                    .customRenderer(GregRenderer::new)
                    .eggColor(0xAC3232, FOLIANT_EGG_COLOR)
                    .build();

    public static final EntityRegistryEntry<AsteirtoEntity> ASTEIRTO =
            MANAGER.<AsteirtoEntity>createBuilder()
                    .withTypeSupplier(
                    () -> EntityType.Builder.of(AsteirtoEntity::new, MobCategory.MONSTER)
                            .sized(2, 2.5f))
                    .withName("asteirto")
                    .withAttributes(AsteirtoEntity::createAttributes)
                    .customRenderer(AsteirtoRenderer::new)
                    .eggColor(0x9BE468, FOLIANT_EGG_COLOR)
                    .build();

    public static final EntityRegistryEntry<AmelieEntity> AMELIE =
            MANAGER.<AmelieEntity>createBuilder()
                    .withTypeSupplier(
                    () -> EntityType.Builder.of(AmelieEntity::new, MobCategory.MONSTER)
                            .sized(2, 2.5f))
                    .withName("amelie")
                    .withAttributes(AmelieEntity::createAttributes)
                    .customRenderer(AmelieRenderer::new)
                    .eggColor(0x9BE468, FOLIANT_EGG_COLOR)
                    .build();

    public static final EntityRegistryEntry<ThrownSemisolidEntity> THROWN_SEMISOLID =
            MANAGER.<ThrownSemisolidEntity>createBuilder()
                    .withTypeSupplier(
                    () -> EntityType.Builder.of(ThrownSemisolidEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f))
                    .withName("thrown_item")
                    .customRenderer(context -> new ThrownItemRenderer<>(context, 1, true))
                    .build();

    private static void addAttributeRequest(Consumer<EntityAttributeCreationEvent> consumer) {
        attributeCreationRequests.add(consumer);
    }

    public static void addAttributeRequest(RegistryObject<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier> supplier) {
        addAttributeRequest(event -> {
            //LOGGER.info("Attempting to register attributes for " + entityType.get());
            event.put(entityType.get(), supplier.get());

            //LOGGER.info("Success!");
        });
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        //LOGGER.info(attributeCreationRequests.size() + " attributes were registered");
        for (Consumer<EntityAttributeCreationEvent> consumer : attributeCreationRequests) consumer.accept(event);
    }
}
