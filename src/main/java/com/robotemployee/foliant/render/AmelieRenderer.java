package com.robotemployee.foliant.render;

import com.robotemployee.foliant.entity.AmelieEntity;
import com.robotemployee.foliant.model.AmelieEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AmelieRenderer extends FoliantRenderer<AmelieEntity> {
    public AmelieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AmelieEntityModel());
    }


}
