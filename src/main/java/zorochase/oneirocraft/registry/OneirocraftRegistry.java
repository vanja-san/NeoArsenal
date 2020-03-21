package zorochase.oneirocraft.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ScatteredPlantFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import zorochase.oneirocraft.block.CustomBerryBushBlock;
import zorochase.oneirocraft.world.features.OneiricBerryBushFeature;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OneirocraftRegistry {

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ModItems.ONEIRIC_BERRIES,
                ModItems.FABRICATOR,
                ModItems.DREAMSTEEL_INGOT,
                ModItems.DREAMSTEEL_NUGGET,
                ModItems.DREAMSTEEL_ROD,
                ModItems.WISH_SLIP,
                ModItems.DREAMSTEEL_SWORD,
                ModItems.DREAMSTEEL_PICKAXE,
                ModItems.DREAMSTEEL_AXE,
                ModItems.DREAMSTEEL_SHOVEL
        );
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                ModBlocks.ONEIRIC_BERRY_BUSH_BLOCK
        );
    }

    @SubscribeEvent
    public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> registry = event.getRegistry();

        ModFeatures.ONEIRIC_BERRY_BUSH_FEATURE = ModFeatures.register(registry, new OneiricBerryBushFeature(), "oneiric_berry_bush");

        ModFeatures.addFeatures();
    }

}
