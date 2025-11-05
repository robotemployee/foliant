package com.robotemployee.foliant.model;

import com.robotemployee.foliant.Foliant;
import com.robotemployee.foliant.entity.AmelieEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AmelieEntityModel extends GeoModel<AmelieEntity> {
    @Override
    public ResourceLocation getModelResource(AmelieEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "geo/amelie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AmelieEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "textures/entity/amelie/amelie.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AmelieEntity animatable) {
        return new ResourceLocation(Foliant.MODID, "animations/amelie.animation.json");
    }
}
