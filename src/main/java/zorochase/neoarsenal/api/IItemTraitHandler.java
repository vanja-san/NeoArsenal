package zorochase.neoarsenal.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Set;

/**
 * Item "traits" are flags applicable to specific item types, like tools.
 * They can be used to grant advantages (or disadvantages) to an item,
 * like apply a specific set of enchantments or allow the item to perform
 * a specific action. Item traits are stored via ItemStack NBT.
 */
public interface IItemTraitHandler {

    /**
     * @return Whether the stack has traits.
     */
    boolean hasTraits(ItemStack stack);

    /**
     * @return The NBT tag storing the stack's traits.
     */
    CompoundNBT getTraitsTag(ItemStack stack);

    /**
     * Adds a trait to a stack.
     *
     * @param identifier The trait's name.
     */
    void addTrait(ItemStack stack, String identifier);

    /**
     * Adds a Set of traits to a stack all at once.
     *
     * @param identifiers The set of trait identifiers (names) to iterate.
     */
    void addAll(ItemStack stack, Set<String> identifiers);

    /**
     * Initializes a stack with a default trait.
     */
    void initialize(ItemStack stack);

    /**
     * Initializes a stack with a Set of traits.
     *
     * @param withDefault Whether to include the default trait
     */
    void initialize(ItemStack stack, Set<String> identifiers, boolean withDefault);

}
