package com.robotemployee.foliant.registry;

import com.mojang.logging.LogUtils;
import com.robotemployee.foliant.Foliant;
import com.robotemployee.foliant.item.SemisolidItem;
import com.robotemployee.reu.util.registry.builder.ItemBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

public class ModItems {

    // note that this is not the only place items are registered.
    // for example, items are registered by BlockBuilder in order to automatically make block items
    // they are also registered by FluidBuilder if a bucket and bottle for the fluid are required
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Foliant.MODID);
    //public static final RegistryObject<Item> RECONSTRUCTOR = ITEMS.register("reconstructor", () -> new ReconstructorItem(new Item.Properties().rarity(Rarity.RARE)));
    //public static final RegistryObject<Item> SCULK_RECONSTRUCTOR = ITEMS.register("sculk_reconstructor", () -> new SculkReconstructorItem(new Item.Properties().rarity(Rarity.EPIC)));
    //public static final RegistryObject<Item> INJECTOR = ITEMS.register("injector", () -> new InjectorItem(new Item.Properties()));

    public static final ItemBuilder.Manager MANAGER = new ItemBuilder.Manager(Foliant.DATAGEN, ITEMS)
            .defaultCreativeTab(ModCreativeModeTabs.CREATIVE_TAB);

    static Logger LOGGER = LogUtils.getLogger();

    public static final RegistryObject<Item> SEMISOLID = MANAGER.createBuilder()
            .withName("semisolid")
            .withSupplier(() -> new SemisolidItem(new Item.Properties()
                    .durability(SemisolidItem.DECAY_SECONDS)
                    .setNoRepair()
                    .rarity(Rarity.RARE)
            ))
            .build();

}
