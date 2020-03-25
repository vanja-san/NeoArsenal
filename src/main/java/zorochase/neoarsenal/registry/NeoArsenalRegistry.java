package zorochase.neoarsenal.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import zorochase.neoarsenal.world.features.EnergeticBerryBushFeature;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeoArsenalRegistry {

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ModItems.ENERGETIC_BERRIES,
                ModItems.FABRICATOR,
                ModItems.STEEL_INGOT,
                ModItems.STEEL_NUGGET,
                ModItems.STEEL_ROD,
                ModItems.WISH_SLIP,
                ModItems.MORPHING_STEEL_SWORD,
                ModItems.MORPHING_STEEL_PICKAXE,
                ModItems.MORPHING_STEEL_AXE,
                ModItems.MORPHING_STEEL_SHOVEL
        );
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                ModBlocks.ENERGETIC_BERRY_BUSH_BLOCK
        );
    }

    @SubscribeEvent
    public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> registry = event.getRegistry();

        ModFeatures.ENERGETIC_BERRY_BUSH_FEATURE = ModFeatures.register(registry, new EnergeticBerryBushFeature(), "energeticic_berry_bush");

        ModFeatures.addFeatures();
    }

}
