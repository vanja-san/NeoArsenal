package zorochase.oneirocraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/*
 * Methods for dealing with ITextComponents
 */
public class LoreHelper {

    public static ITextComponent loreString(String text) {
        return new TranslationTextComponent(text).applyTextStyle(TextFormatting.GRAY);
    }

    public static ITextComponent loreString(String text, TextFormatting... styles) {
        return loreString(text).applyTextStyles(styles);
    }

    public static ITextComponent loreInt(int value) {
        return new TranslationTextComponent(String.valueOf(value));
    }

    public static ITextComponent loreInt(int value, TextFormatting... styles) {
        return loreInt(value).applyTextStyles(styles);
    }

    public static ITextComponent loreInt(int value, int... values) {
        TextFormatting color = TextFormatting.WHITE;
        TextFormatting[] colors = new TextFormatting[]{TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN};
        for (int i = 0; i < values.length; i++) {
            if (value >= values[i]) color = colors[i];
        }
        return loreInt(value).applyTextStyle(color);
    }

    public static ITextComponent newLine() {
        return new TranslationTextComponent(" ");
    }

    public static ITextComponent shiftForInfo() {
        return loreString("LSHIFT", TextFormatting.AQUA).appendSibling(loreString(" -> more info", TextFormatting.GRAY));
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
}