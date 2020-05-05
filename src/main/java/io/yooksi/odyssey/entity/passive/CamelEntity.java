package io.yooksi.odyssey.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.world.World;

public class CamelEntity
    extends LlamaEntity {

  public static final String NAME = "camel";

  public CamelEntity(EntityType<? extends LlamaEntity> entityType, World world) {

    super(entityType, world);
  }
}
