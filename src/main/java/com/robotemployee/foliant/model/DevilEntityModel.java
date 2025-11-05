package com.robotemployee.foliant.model;

import com.robotemployee.foliant.Foliant;
import com.robotemployee.foliant.entity.DevilEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class DevilEntityModel extends GeoModel<DevilEntity> {

    @Override
    public ResourceLocation getModelResource(DevilEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "geo/devil.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DevilEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "textures/entity/devil/devil.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DevilEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "animations/devil.animation.json");
    }
}
