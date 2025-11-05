package com.robotemployee.foliant.model;

import com.robotemployee.foliant.Foliant;
import com.robotemployee.foliant.entity.GregEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class GregEntityModel extends GeoModel<GregEntity> {

    @Override
    public ResourceLocation getModelResource(GregEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "geo/greg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GregEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "textures/entity/greg/greg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GregEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "animations/greg.animation.json");
    }
}
