package io.yooksi.odyssey.entity;

import io.yooksi.odyssey.common.Defines;
import io.yooksi.odyssey.entity.passive.CamelEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Defines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityTypes {

  private static EntityType<CamelEntity> CAMEL_ENTITY_TYPE;

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Defines.MODID);

  public static final RegistryObject<EntityType<CamelEntity>> CAMEL = ENTITY_TYPES.register(CamelEntity.NAME, () -> CAMEL_ENTITY_TYPE);

  @SubscribeEvent
  public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

    /*
     * The event to register items is fired before the event to register entity
     * types, therefore, if we register an entity spawn egg before the entity
     * type is created, we will experience a NPE when the egg is used in game.
     *
     * We can circumvent the error if we create the entity type here, during
     * the item registration event.
     *
     * Reference: https://www.minecraftforge.net/forum/topic/75045-solved1144-entities-and-spawneggs/
     *
     * ---
     *
     * The following is complete misinformation and there is no way to suppress
     * the log warnings about data-fixers for entities.
     *
     * The String parameter of the builder’s build method is a data-fixer id.
     * Data fixers do not work with mods (yet) so you should pass null in.
     *
     * Passing null will suppress the log warning of no data fixers registered.
     *
     * Source: https://mcforge.readthedocs.io/en/1.15.x/concepts/registries/
     */

    CAMEL_ENTITY_TYPE = EntityType.Builder.create(CamelEntity::new, EntityClassification.CREATURE)
        .size(EntityType.LLAMA.getWidth(), EntityType.LLAMA.getHeight())
        .build(new ResourceLocation(Defines.MODID, CamelEntity.NAME).toString());

    {
      int primaryColor = new Color(165, 148, 108).getRGB();
      int secondaryColor = new Color(77, 68, 48).getRGB();
      event.getRegistry().register(
          new SpawnEggItem(CAMEL_ENTITY_TYPE, primaryColor, secondaryColor, new Item.Properties().group(ItemGroup.MISC))
              .setRegistryName(new ResourceLocation(Defines.MODID, CamelEntity.NAME + "_spawn_egg").toString())
      );
    }
  }
}
