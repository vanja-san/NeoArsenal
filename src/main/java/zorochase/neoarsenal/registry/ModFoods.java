package zorochase.neoarsenal.registry;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

    public static final Food ENERGETIC_BERRIES = (
            new Food.Builder())
            .hunger(2)
            .saturation(0.1F)
            .setAlwaysEdible()
            .effect(new EffectInstance(Effects.POISON, 100, 0), 0.5f)
            .build();
}
