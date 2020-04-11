package zorochase.neoarsenal.api;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Set;

import static zorochase.neoarsenal.api.NeoArsenalTraits.*;

/**
 * This is one implementation of IItemTraitHandler. It cycles through
 * a child tag containing integer values; these values are used as indices,
 * such that only the trait with the index that matches the selected index
 * will be applied. The selected index is iterated with a keypress. A cooldown
 * is applied to they key so traits aren't switched too quickly.
 */
public class CycledTraitHandler implements IItemTraitHandler {

    private final String TRAITS_LIST = "CycledTraits";
    private final String ACTIVE_TRAIT_TAG = "ActiveTrait";
    private final String COOLDOWN_TAG = "Cooldown";

    @Override
    public boolean hasTraits(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().contains(TRAITS_LIST, Constants.NBT.TAG_COMPOUND) && stack.getTag().contains(ACTIVE_TRAIT_TAG, Constants.NBT.TAG_INT);
        }
        return false;
    }

    @Override
    public CompoundNBT getTraitsTag(ItemStack stack) {
        return stack.getOrCreateChildTag(TRAITS_LIST);
    }

    public int getActiveTraitIndex(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ACTIVE_TRAIT_TAG);
    }

    public String getActiveTraitIdentifier(ItemStack stack) {
        int activeIndex = getActiveTraitIndex(stack);
        for (String key : getTraitsTag(stack).keySet()) {
            if (getTraitsTag(stack).getInt(key) == activeIndex) return key;
        }
        return NONE.getIdentifier();
    }

    public int getCooldown(ItemStack stack) {
        return getCooldown(stack, COOLDOWN_TAG);
    }

    // For items that have more than one cooldown to handle
    public int getCooldown(ItemStack stack, String tagKey) {
        return stack.getOrCreateTag().getInt(tagKey);
    }

    @Override
    public void addTrait(ItemStack stack, String identifier) {
        int size = getTraitsTag(stack).size();
        getTraitsTag(stack).putInt(identifier, size);
    }

    @Override
    public void addAll(ItemStack stack, Set<String> identifiers) {
        Object[] keysArray = identifiers.toArray();
        for (Object o : keysArray) {
            addTrait(stack, (String) o);
        }
    }

    public void setActiveTraitIndex(ItemStack stack, int index) {
        stack.getOrCreateTag().putInt(ACTIVE_TRAIT_TAG, index);
    }

    public void setActiveTraitIndex(ItemStack stack, String identifier) {
        setActiveTraitIndex(stack, getTraitsTag(stack).getInt(identifier));
    }

    public void iterateActiveTraitIndex(ItemStack stack) {
        int size = getTraitsTag(stack).size();
        int activeIndex = getActiveTraitIndex(stack);
        activeIndex += (activeIndex + 1) < size ? 1 : -activeIndex;
        setActiveTraitIndex(stack, activeIndex);
    }

    public void setCooldown(ItemStack stack, int time) {
        setCooldown(stack, time, COOLDOWN_TAG);
    }

    // For items that have more than one cooldown to handle
    public void setCooldown(ItemStack stack, int time, String tagKey) {
        stack.getOrCreateTag().putInt(tagKey, Math.max(0, time));
    }

    @Override
    public void initialize(ItemStack stack) {
        addTrait(stack, NONE.getIdentifier());
        setActiveTraitIndex(stack, 0);
        setCooldown(stack, 0);
    }

    @Override
    public void initialize(ItemStack stack, Set<String> identifiers, boolean withDefault) {
        if (withDefault) addTrait(stack, NONE.getIdentifier());
        addAll(stack, identifiers);
        setActiveTraitIndex(stack, 0);
        setCooldown(stack, 0);
    }

    public float identifierAsFloat(ItemStack stack) {
        String identifier = getActiveTraitIdentifier(stack);
        return (float) NeoArsenalTraits.valueOf(identifier.toUpperCase()).ordinal();
    }

    public void setEnchantmentsForTrait(ItemStack stack) {
        Item item = stack.getItem();
        stack.getEnchantmentTagList().clear();
        String identifier = getActiveTraitIdentifier(stack);

        if (identifier.equals(DEADLY.getIdentifier())) {
            stack.addEnchantment(Enchantments.SHARPNESS, 6);
            stack.addEnchantment(Enchantments.KNOCKBACK, 3);
            if (item instanceof SwordItem) stack.addEnchantment(Enchantments.SWEEPING, 4);
        } else if (identifier.equals(SILKY.getIdentifier())) {
            stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
        } else if (identifier.equals(LUCKY.getIdentifier())) {
            if (item instanceof SwordItem || item instanceof AxeItem) stack.addEnchantment(Enchantments.LOOTING, 4);
            else stack.addEnchantment(Enchantments.FORTUNE, 3);
        } else if (identifier.equals(INCENDIARY.getIdentifier())) {
            stack.addEnchantment(Enchantments.FIRE_ASPECT, 3);
        } else if (identifier.equals(EFFICIENT.getIdentifier())) {
            stack.addEnchantment(Enchantments.EFFICIENCY, 6);
        } else if (identifier.equals(MASSIVE.getIdentifier())) {
            stack.addEnchantment(Enchantments.KNOCKBACK, 2);
        }
    }

    public boolean toolSupportsTrait(ItemStack stack, String identifier) {
        Item tool = stack.getItem();

        if (identifier.equals(INCENDIARY.getIdentifier()) || identifier.equals(DEADLY.getIdentifier())) {
            return (tool instanceof SwordItem || tool instanceof AxeItem);
        } else if (identifier.equals(LUCKY.getIdentifier()) || identifier.equals(SILKY.getIdentifier())) {
            return (tool instanceof SwordItem || tool instanceof AxeItem || tool instanceof PickaxeItem || tool instanceof ShovelItem);
        } else if (identifier.equals(EFFICIENT.getIdentifier())) {
            return (tool instanceof PickaxeItem || tool instanceof ShovelItem || tool instanceof AxeItem);
        } else if (identifier.equals(MASSIVE.getIdentifier())) {
            return (tool instanceof PickaxeItem || tool instanceof ShovelItem);
        } else {
            return false;
        }
    }
}
