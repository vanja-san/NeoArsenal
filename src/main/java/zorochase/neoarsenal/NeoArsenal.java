package zorochase.neoarsenal;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zorochase.neoarsenal.network.Network;
import zorochase.neoarsenal.registry.ModBlocks;
import zorochase.neoarsenal.registry.ModItems;
import zorochase.neoarsenal.world.OreGen;

@Mod(NeoArsenal.MOD_ID)
public class NeoArsenal {

    public static NeoArsenal INSTANCE;
    public static final String MOD_ID = "neoarsenal";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup MOD_GROUP = new NeoArsenalItemGroup();

    public NeoArsenal() {
        INSTANCE = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGen.setup();
        Network.registerMessages();
    }
}
