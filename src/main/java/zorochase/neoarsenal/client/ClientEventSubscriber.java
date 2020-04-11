package zorochase.neoarsenal.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.item.PortableChargerItem;
import zorochase.neoarsenal.network.Network;
import zorochase.neoarsenal.network.SyncTraitsPacket;
import zorochase.neoarsenal.registry.ModSoundEvents;
import zorochase.neoarsenal.util.LoreHelper;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;

@Mod.EventBusSubscriber(modid = NeoArsenal.MOD_ID, value = CLIENT)
public class ClientEventSubscriber {

    private static final String CATEGORY = "key.categories." + NeoArsenal.MOD_ID;

    private static final KeyBinding CYCLE_TRAITS = new KeyBinding(NeoArsenal.MOD_ID + ".key.cycleTraits", GLFW_KEY_O, CATEGORY);

    static {
        ClientRegistry.registerKeyBinding(CYCLE_TRAITS);
    }

    private static void cycleTraits(ItemStack stack, PlayerEntity player) {
        CycledTraitHandler cycledTraitHandler = new CycledTraitHandler();
        SoundEvent sound = stack.getItem() instanceof PortableChargerItem ? ModSoundEvents.CYCLE_MODES : ModSoundEvents.CYCLE_TRAITS;
        String title = stack.getItem() instanceof PortableChargerItem ? "Mode: " : "Trait: ";
        if (cycledTraitHandler.getCooldown(stack) == 0) {
            cycledTraitHandler.iterateActiveTraitIndex(stack);
            cycledTraitHandler.setEnchantmentsForTrait(stack);
            player.playSound(sound, 1.0F, 2.0F);
            player.sendStatusMessage(LoreHelper.loreString(title + cycledTraitHandler.getActiveTraitIdentifier(stack), TextFormatting.AQUA), true);
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
                    if ((storage.getEnergyStored() > 0 || held.getItem() instanceof PortableChargerItem) && cycledTraitHandler.getTraitsTag(held).size() > 1) {
                        cycleTraits(held, player);
                    } else {
                        if (cycledTraitHandler.getCooldown(held) == 0) {
                            cycledTraitHandler.setCooldown(held, 20);
                            player.playSound(ModSoundEvents.CYCLE_DENIED, 1.0F, 4.0F);
                        }
                    }
                } else {
                    if (cycledTraitHandler.getTraitsTag(held).size() > 1) {
                        cycleTraits(held, player);
                    }
                }
                Network.INSTANCE.sendToServer(new SyncTraitsPacket(held));
            }
        }
    }
}
