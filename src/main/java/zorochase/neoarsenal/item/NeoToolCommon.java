package zorochase.neoarsenal.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import zorochase.neoarsenal.api.CycledTraitHandler;
import zorochase.neoarsenal.api.NeoArsenalTraits;

import javax.annotation.Nullable;
import java.util.List;

import static zorochase.neoarsenal.util.LoreHelper.*;

class NeoToolCommon {

    static final int CAPACITY = 5000;
    static final int MAX_TRANSFER = 15;

    static final String ENERGY_TAG = "Energy";

    public static float getChargeRatio(ItemStack stack) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(IllegalStateException::new);
            return (float) storage.getEnergyStored() / storage.getMaxEnergyStored();
        }
        return 0;
    }


    static Rarity getRarity(CycledTraitHandler cycledTraitHandler, ItemStack stack) {
        return cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.NONE.getIdentifier()) ? Rarity.UNCOMMON : Rarity.RARE;
    }

    static boolean onWeaponLeftclickEntity(ItemStack stack, PlayerEntity player) {
        if (!player.isCreative()) {
            LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
            if (lazy.isPresent()) {
                IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                return !(storage.getEnergyStored() > 0);
            }
        }
        return false;
    }

    static boolean hitEntity(CycledTraitHandler cycledTraitHandler, ItemStack stack, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (!player.isCreative()) {
                stack.getOrCreateTag().putInt("JustHitEntity", 1);
                cycledTraitHandler.setCooldown(stack, 20);
                LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
                if (lazy.isPresent()) {
                    IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
                    if (storage.getEnergyStored() > 0) {
                        if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.NONE.getIdentifier()))
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 8, false);
                        else {
                            storage.extractEnergy(NeoToolCommon.MAX_TRANSFER - 2, false);
                        }
                    }
                }
            }
        }
        return true;
    }

    static void inventoryTick(CycledTraitHandler cycledTraitHandler, ItemStack stack) {
        if (!stack.hasTag() || !cycledTraitHandler.hasTraits(stack)) cycledTraitHandler.initialize(stack);
        cycledTraitHandler.setCooldown(stack, cycledTraitHandler.getCooldown(stack) - 1);
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            if (storage.getEnergyStored() == 0) {
                cycledTraitHandler.setActiveTraitIndex(stack, NeoArsenalTraits.NONE.getIdentifier());
                stack.getEnchantmentTagList().clear();
            }
        }
    }

    static boolean shouldCauseReequipAnimation(CycledTraitHandler cycledTraitHandler, ItemStack oldStack) {
        return cycledTraitHandler.getCooldown(oldStack) > 0;
    }

    static void addInformation(CycledTraitHandler cycledTraitHandler, ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY == null) return;

        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            tooltip.add(chargeRatio(stack));
            if (storage.getEnergyStored() == 0) {
                tooltip.add(loreString("No energy stored! Energy is required to be useful!", TextFormatting.RED));
            }
            tooltip.add(newLine());
        }
        if (cycledTraitHandler.hasTraits(stack)) {
            if (cycledTraitHandler.getActiveTraitIdentifier(stack).equals(NeoArsenalTraits.NONE.getIdentifier())) {
                tooltip.add(loreString("No trait currently applied...", TextFormatting.GRAY));
                if (cycledTraitHandler.getTraitsTag(stack).size() < 2) {
                    tooltip.add(loreString("Tip: you'll need a Upgrade Schematic to add a trait!", TextFormatting.DARK_GRAY));
                } else {
                    tooltip.add(loreString("Available traits: "));
                    for (String s : cycledTraitHandler.getTraitsTag(stack).keySet()) {
                        if (!s.equals(NeoArsenalTraits.NONE.getIdentifier())) tooltip.add(loreString(" - " + s));
                    }
                }
            } else {
                tooltip.add(activeTrait(stack, cycledTraitHandler, "Trait applied: "));
            }
        }
    }

    static boolean showDurabilityBar(ItemStack stack) {
        LazyOptional<IEnergyStorage> lazy = stack.getCapability(CapabilityEnergy.ENERGY);
        if (lazy.isPresent()) {
            IEnergyStorage storage = lazy.orElseThrow(AssertionError::new);
            return storage.getEnergyStored() != storage.getMaxEnergyStored();
        }
        return false;
    }

    static double getDurabilityForDisplay(ItemStack stack) {
        return 1 - getChargeRatio(stack);
    }
}
