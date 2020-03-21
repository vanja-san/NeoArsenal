package zorochase.oneirocraft.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import zorochase.oneirocraft.api.CycledTraitHandler;

import java.util.function.Supplier;

public class SyncTraitsPacket {

    private ItemStack messageStack;

    public SyncTraitsPacket(PacketBuffer buf) {
        messageStack = buf.readItemStack();
    }

    public SyncTraitsPacket(ItemStack stack) {
        this.messageStack = stack;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeItemStack(messageStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(
                () -> {
                    PlayerEntity player = ctx.get().getSender();
                    if (player != null) {
                        ItemStack held = player.getHeldItemMainhand();
                        CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
                        if (cycledTraitHandler.hasTraits(messageStack)) {
                            cycledTraitHandler.setActiveTraitIndex(held, cycledTraitHandler.getActiveTraitIdentifier(messageStack));
                            cycledTraitHandler.setCooldown(held, cycledTraitHandler.getCooldown(messageStack));
                            cycledTraitHandler.setEnchantmentsForTrait(held);
                        }
                    }
                }
        );
        ctx.get().setPacketHandled(true);
    }
}
