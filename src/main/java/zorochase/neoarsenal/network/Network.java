package zorochase.neoarsenal.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import zorochase.neoarsenal.NeoArsenal;

public class Network {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() { return ID++; }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(NeoArsenal.MOD_ID, "neoarsenal_network"), () -> "1.0", s -> true, s -> true);
        INSTANCE.registerMessage(nextID(), SyncTraitsPacket.class, SyncTraitsPacket::toBytes, SyncTraitsPacket::new, SyncTraitsPacket::handle);
    }

    public static SimpleChannel getInstance() { return INSTANCE; }


}
