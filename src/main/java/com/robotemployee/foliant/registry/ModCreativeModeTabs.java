package com.robotemployee.foliant.registry;

import com.robotemployee.foliant.Foliant;
import com.robotemployee.reu.util.registry.builder.CreativeTabBuilder;
import com.robotemployee.reu.util.registry.entry.CreativeTabMutableRegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Foliant.MODID);

    public static final CreativeTabBuilder.Manager MANAGER = new CreativeTabBuilder.Manager(CREATIVE_MODE_TABS, Foliant.MODID);

    public static final CreativeTabMutableRegistryEntry CREATIVE_TAB = MANAGER.createBuilder()
            .withName(Foliant.MODID)
            .withIcon(() -> new ItemStack(ModItems.SEMISOLID.get()))
            .withTitle(Component.literal("Foliant"))
            .build();
}
