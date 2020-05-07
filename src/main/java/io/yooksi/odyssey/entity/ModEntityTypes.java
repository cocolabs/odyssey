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

/**
 * This class is responsible for registering both {@link EntityType}s and {@link SpawnEggItem}s.
 * <p>
 * It uses the item registration event to create the entity types as well as
 * register the spawn eggs. The types are created in the item registration event
 * because items are registered before entity types and the entity type is
 * needed to register the spawn egg item. If the entity types are created in the
 * deferred registration's supplier, it will cause a NPE when the spawn egg is
 * used.
 * <p>
 * Reference: https://www.minecraftforge.net/forum/topic/75045-solved1144-entities-and-spawneggs/
 * <p>
 * -----------------------------------------------------------------------------
 * <p>
 * The following is incorrect and I couldn't find a way to suppress the log warnings
 * about missing data-fixers for entities:
 * <p>
 * The String parameter of the builder’s build method is a data-fixer id.
 * Data fixers do not work with mods (yet) so you should pass null in.
 * <p>
 * Passing null will suppress the log warning of no data fixers registered.
 * <p>
 * Source: https://mcforge.readthedocs.io/en/1.15.x/concepts/registries/
 */
@Mod.EventBusSubscriber(modid = Defines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityTypes {

  private static EntityType<CamelEntity> CAMEL_ENTITY_TYPE;

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Defines.MODID);

  public static final RegistryObject<EntityType<CamelEntity>> CAMEL = ENTITY_TYPES.register(CamelEntity.NAME, () -> CAMEL_ENTITY_TYPE);

  @SubscribeEvent
  public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

    CAMEL_ENTITY_TYPE = EntityType.Builder.create(CamelEntity::new, EntityClassification.CREATURE)
        .size(1.2F, 2.3375f)
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
