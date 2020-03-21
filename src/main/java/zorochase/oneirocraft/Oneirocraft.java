package zorochase.oneirocraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zorochase.oneirocraft.network.OneirocraftNetwork;

@Mod(Oneirocraft.MOD_ID)
public class Oneirocraft {

    public static Oneirocraft INSTANCE;
    public static final String MOD_ID = "oneirocraft";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final OneirocraftItemGroup MOD_GROUP = new OneirocraftItemGroup();

    public Oneirocraft() {
        INSTANCE = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OneirocraftNetwork.registerMessages();
    }
}
