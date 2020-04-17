package zorochase.neoarsenal.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.neoarsenal.api.CycledTraitHandler;

import java.text.NumberFormat;
import java.util.Locale;

public class LoreHelper {

    public static ITextComponent loreString(String text) {
        return new TranslationTextComponent(text).applyTextStyle(TextFormatting.GRAY);
    }

    public static ITextComponent loreString(String text, TextFormatting... styles) {
        return loreString(text).applyTextStyles(styles);
    }

    public static ITextComponent loreInt(int value) {
        return new TranslationTextComponent(NumberFormat.getNumberInstance(Locale.US).format(value));
    }

    public static ITextComponent loreInt(int value, TextFormatting... styles) {
        return loreInt(value).applyTextStyles(styles);
    }

    public static ITextComponent newLine() {
        return new TranslationTextComponent(" ");
    }

    public static ITextComponent chargeRatio(ItemStack stack) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElse(null);
            TextFormatting storedColor = storage.getEnergyStored() != 0 ? TextFormatting.AQUA : TextFormatting.RED;
            return loreInt(storage.getEnergyStored(), storedColor)
                    .appendSibling(loreString(" FE / ", TextFormatting.GRAY)
                            .appendSibling(loreInt(storage.getMaxEnergyStored(), TextFormatting.AQUA))
                            .appendSibling(loreString(" FE", TextFormatting.GRAY)));
        }
        return loreString("");
    }

    public static ITextComponent activeTrait(ItemStack stack, CycledTraitHandler cycledTraitHandler, String flavor) {
        return loreString(flavor).appendSibling(loreString("neoarsenal.trait." + cycledTraitHandler.getActiveTraitIdentifier(stack).toLowerCase(), TextFormatting.AQUA));
    }
}