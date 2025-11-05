package com.robotemployee.foliant.render;

import com.robotemployee.foliant.entity.GregEntity;
import com.robotemployee.foliant.model.GregEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GregRenderer extends FoliantRenderer<GregEntity> {
    public GregRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GregEntityModel());
    }
}
