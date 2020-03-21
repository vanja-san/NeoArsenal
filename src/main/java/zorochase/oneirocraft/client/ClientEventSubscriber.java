package zorochase.oneirocraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import zorochase.oneirocraft.Oneirocraft;
import zorochase.oneirocraft.api.CycledTraitHandler;
import zorochase.oneirocraft.item.WishSlipItem;
import zorochase.oneirocraft.network.OneirocraftNetwork;
import zorochase.oneirocraft.network.SyncTraitsPacket;
import zorochase.oneirocraft.util.LoreHelper;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;

@Mod.EventBusSubscriber(modid = Oneirocraft.MOD_ID, value = CLIENT)
public class ClientEventSubscriber {

    private static final String CATEGORY = "key.categories." + Oneirocraft.MOD_ID;

    private static final KeyBinding CYCLE_TRAITS = new KeyBinding(Oneirocraft.MOD_ID + ".key.cycleTraits", GLFW_KEY_O, CATEGORY);

    static {
        ClientRegistry.registerKeyBinding(CYCLE_TRAITS);
    }

    private static void cycleTraits(ItemStack stack, PlayerEntity player) {
        CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
        if (cycledTraitHandler.getCooldown(stack) == 0) {
            cycledTraitHandler.iterateActiveTraitIndex(stack);
            cycledTraitHandler.setEnchantmentsForTrait(stack);
            if (!(stack.getItem() instanceof WishSlipItem)) {
                player.playSound(SoundEvents.BLOCK_BELL_RESONATE, 1.0F, 2.0F);
            } else {
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
            player.sendStatusMessage(LoreHelper.loreString("Trait: " + cycledTraitHandler.getActiveTraitIdentifier(stack), TextFormatting.AQUA), true);
            cycledTraitHandler.setCooldown(stack, 20);
        }
    }

    @SubscribeEvent
    public static void clientTick(final TickEvent.ClientTickEvent event) {
        if (CYCLE_TRAITS.isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
            CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();

            if (cycledTraitHandler.hasTraits(held)) {
                LazyOptional<IEnergyStorage> lazy = held.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (storage.getEnergyStored() > 0 && cycledTraitHandler.getTraitsTag(held).size() > 1) {
                        cycleTraits(held, player);
                    } else {
                        if (cycledTraitHandler.getCooldown(held) == 0) {
                            cycledTraitHandler.setCooldown(held, 20);
                            player.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 4.0F);
                        }
                    }
                } else {
                    if (cycledTraitHandler.getTraitsTag(held).size() > 1) {
                        cycleTraits(held, player);
                    }
                }
                OneirocraftNetwork.INSTANCE.sendToServer(new SyncTraitsPacket(held));
            }
        }
    }
}
