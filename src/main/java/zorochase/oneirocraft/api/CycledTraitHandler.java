package zorochase.oneirocraft.api;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import zorochase.oneirocraft.item.WishSlipItem;

import java.util.Set;

/**
 * This is one implementation of IItemTraitHandler. It cycles through
 * a child tag containing integer values; these values are used as indices,
 * such that only the trait with the index that matches the selected index
 * will be applied. The selected index is iterated with a keypress. A cooldown
 * is applied to they key so traits aren't switched too quickly.
 */
public class CycledTraitHandler implements IItemTraitHandler {

    public final String NONE = "None"; // Used to indicate when no trait is applied
    public final String DEADLY = "Deadly";
    public final String SILKY = "Silky";
    public final String LUCKY = "Lucky";
    public final String INCENDIARY = "Incendiary";
    public final String EFFICIENT = "Efficient";
    public final String MASSIVE = "Massive";

    public final String TRAITS_LIST = "CycledTraits";
    public final String ACTIVE_TRAIT = "ActiveTrait";
    public final String COOLDOWN = "Cooldown";

    @Override
    public boolean hasTraits(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().contains(TRAITS_LIST, Constants.NBT.TAG_COMPOUND) && stack.getTag().contains(ACTIVE_TRAIT, Constants.NBT.TAG_INT);
        }
        return false;
    }

    @Override
    public CompoundNBT getTraitsTag(ItemStack stack) {
        return stack.getOrCreateChildTag(TRAITS_LIST);
    }

    public int getActiveTraitIndex(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ACTIVE_TRAIT);
    }

    public String getActiveTraitIdentifier(ItemStack stack) {
        int activeIndex = getActiveTraitIndex(stack);
        for (String key : getTraitsTag(stack).keySet()) {
            if (getTraitsTag(stack).getInt(key) == activeIndex) return key;
        }
        return NONE;
    }

    public int getCooldown(ItemStack stack) {
        return stack.getOrCreateTag().getInt(COOLDOWN);
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
        stack.getOrCreateTag().putInt(ACTIVE_TRAIT, index);
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
        stack.getOrCreateTag().putInt(COOLDOWN, Math.max(0, time));
    }

    @Override
    public void initialize(ItemStack stack) {
        addTrait(stack, NONE);
        setActiveTraitIndex(stack, 0);
        setCooldown(stack, 0);
    }

    @Override
    public void initialize(ItemStack stack, Set<String> identifiers, boolean withDefault) {
        if (withDefault) addTrait(stack, NONE);
        addAll(stack, identifiers);
        setActiveTraitIndex(stack,0);
        setCooldown(stack, 0);
    }

    public float identifierAsFloat(ItemStack stack) {
        switch (getActiveTraitIdentifier(stack)) {
            case DEADLY:
                return 1.0F;
            case SILKY:
                return 2.0F;
            case LUCKY:
                return 3.0F;
            case EFFICIENT:
                return 4.0F;
            case INCENDIARY:
                return 5.0F;
            case MASSIVE:
                return 6.0F;
            default:
                return 0.0F;
        }
    }

    public void setEnchantmentsForTrait(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof WishSlipItem) return;

        stack.getEnchantmentTagList().clear();
        switch(getActiveTraitIdentifier(stack)) {
            case DEADLY: {
                stack.addEnchantment(Enchantments.SHARPNESS, 6);
                stack.addEnchantment(Enchantments.KNOCKBACK, 3);
                if (item instanceof SwordItem) stack.addEnchantment(Enchantments.SWEEPING, 4);
                break;
            }
            case SILKY: {
                stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                break;
            }
            case LUCKY: {
                if (item instanceof SwordItem || item instanceof AxeItem) stack.addEnchantment(Enchantments.LOOTING, 4);
                else stack.addEnchantment(Enchantments.FORTUNE, 3);
                break;
            }
            case INCENDIARY: {
                stack.addEnchantment(Enchantments.FIRE_ASPECT, 3);
                break;
            }
            case EFFICIENT: {
                stack.addEnchantment(Enchantments.EFFICIENCY, 6);
                break;
            }
            case MASSIVE: {
                stack.addEnchantment(Enchantments.KNOCKBACK, 2);
            }
        }
    }

    public boolean toolSupportsTrait(ItemStack stack, String identifier) {
        Item tool = stack.getItem();

        switch(identifier) {
            case INCENDIARY:
            case DEADLY: {
                return tool instanceof SwordItem || tool instanceof AxeItem;
            }
            case LUCKY:
            case SILKY: {
                return tool instanceof SwordItem || tool instanceof AxeItem || tool instanceof PickaxeItem || tool instanceof ShovelItem;
            }
            case EFFICIENT: {
                return tool instanceof PickaxeItem || tool instanceof ShovelItem || tool instanceof AxeItem;
            }
            case MASSIVE: {
                return tool instanceof PickaxeItem || tool instanceof ShovelItem;
            }
            default: return false;
        }
    }
}
