package zorochase.neoarsenal;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zorochase.neoarsenal.network.Network;

@Mod(NeoArsenal.MOD_ID)
public class NeoArsenal {

    public static NeoArsenal INSTANCE;
    public static final String MOD_ID = "neoarsenal";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final NeoArsenalItemGroup MOD_GROUP = new NeoArsenalItemGroup();

    public NeoArsenal() {
        INSTANCE = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Network.registerMessages();
    }
}
