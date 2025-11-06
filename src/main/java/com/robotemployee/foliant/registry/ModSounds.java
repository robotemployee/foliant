package com.robotemployee.foliant.registry;

import com.mojang.logging.LogUtils;
import com.robotemployee.foliant.Foliant;
import com.robotemployee.reu.util.registry.builder.SoundBuilder;
import com.robotemployee.reu.util.registry.tools.SoundTools;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

public class ModSounds {

    static Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Foliant.MODID);

    public static final SoundBuilder.Manager MANAGER = new SoundBuilder.Manager(Foliant.DATAGEN, SOUNDS, Foliant.MODID);

    public static final RegistryObject<SoundEvent> GREG_FLYING = SoundTools.registerNormalSound("entity.greg.fly", "entity/greg_flying", MANAGER);

    public static final RegistryObject<SoundEvent> ASTEIRTO_HUM = MANAGER.createBuilder()
            .withName("entity.asteirto.idle")
            .soundLocation("entity/asteirto_hum")
            .soundModifier(sound -> sound.attenuationDistance(32))
            .build();

}
