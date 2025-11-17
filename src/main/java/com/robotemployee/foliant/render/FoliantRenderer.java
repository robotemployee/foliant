package com.robotemployee.foliant.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.robotemployee.foliant.entity.FoliantRaidMob;
import com.robotemployee.reu.util.RenderTools;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class FoliantRenderer<T extends FoliantRaidMob & GeoAnimatable> extends GeoEntityRenderer<T> {

    public static final Logger LOGGER = LogUtils.getLogger();
    public FoliantRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }
}
