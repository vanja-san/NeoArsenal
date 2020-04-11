package zorochase.neoarsenal.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static zorochase.neoarsenal.NeoArsenal.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSoundEvents {

    public static final SoundEvent CYCLE_TRAITS = newSoundEvent("cycle_trait");
    public static final SoundEvent CYCLE_MODES = newSoundEvent("cycle_mode");
    public static final SoundEvent CYCLE_DENIED = newSoundEvent("cycle_denied");
    public static final SoundEvent ADD_TRAIT = newSoundEvent("add_trait");
    public static final SoundEvent ADD_TRAIT_DENIED = newSoundEvent("add_trait_denied");

    public static final SoundEvent COMPACTED_NEO_STEP = newSoundEvent("compacted_neo_step");
    public static final SoundEvent COMPACTED_NEO_BREAK = newSoundEvent("compacted_neo_break");
    public static final SoundEvent COMPACTED_NEO_PLACE = newSoundEvent("compacted_neo_place");
    public static final SoundEvent COMPACTED_NEO_HIT = newSoundEvent("compacted_neo_hit");
    public static final SoundEvent COMPACTED_NEO_FALL = newSoundEvent("compacted_neo_fall");

    private static SoundEvent newSoundEvent(String registryName) {
        ResourceLocation location = new ResourceLocation(MOD_ID, registryName);
        return new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                CYCLE_TRAITS, CYCLE_MODES, CYCLE_DENIED, ADD_TRAIT, ADD_TRAIT_DENIED,
                COMPACTED_NEO_STEP, COMPACTED_NEO_BREAK, COMPACTED_NEO_PLACE, COMPACTED_NEO_FALL
        );
    }

}
