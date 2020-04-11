package zorochase.neoarsenal.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zorochase.neoarsenal.item.crafting.NeoToolRecipe;
import zorochase.neoarsenal.item.crafting.ToolSchematicRecipe;
import zorochase.neoarsenal.item.crafting.UpgradeSchematicRecipe;

import static zorochase.neoarsenal.NeoArsenal.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeSerializers {

    @SubscribeEvent
    public static void registerRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                ToolSchematicRecipe.SERIALIZER,
                NeoToolRecipe.SERIALIZER,
                UpgradeSchematicRecipe.SERIALIZER
        );
    }
}
