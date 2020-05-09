package io.yooksi.odyssey.client.renderer.entity;

import io.yooksi.odyssey.client.renderer.entity.model.CamelModel;
import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CamelRenderer
		extends MobRenderer<CamelEntity, CamelModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Defines.MODID, "textures/entity" +
			"/camel/camel.png");

	public CamelRenderer(EntityRendererManager rendererManager) {

		super(rendererManager, new CamelModel(), 0.7f);
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull CamelEntity entity) {

		return TEXTURE;
	}
}
