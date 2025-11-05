package com.robotemployee.foliant.registry;

import com.robotemployee.foliant.Foliant;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Foliant.MODID);

    // BlockRegistryEntry has both a block and an item

    public static class Patterns {
        //public static final BlockPattern ALTAR
    }
}
