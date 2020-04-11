package zorochase.neoarsenal.item.crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import zorochase.neoarsenal.NeoArsenal;
import zorochase.neoarsenal.item.UpgradeSchematicItem;
import zorochase.neoarsenal.registry.ModItems;

public class UpgradeSchematicRecipe extends SpecialRecipe {

    public static final SpecialRecipeSerializer<UpgradeSchematicRecipe> SERIALIZER = new Serializer();

    public UpgradeSchematicRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    private boolean isTraitItem(Item item) {
        return (item == Items.QUARTZ || item == Items.STRING || item == Items.DIAMOND || item == ModItems.INCENDIARY_EYE.get() || item == Items.GOLD_INGOT || item == Items.IRON_BLOCK);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.BLANK_SLIP.get() && !flag) {
                    flag = true;
                } else if (isTraitItem(stack.getItem()) && !flag1) {
                    flag1 = true;
                } else {
                    if (stack.getItem() != ModItems.TRAIT_ANALYZER.get() || flag2) {
                        return false;
                    }

                    flag2 = true;
                }
            }
        }
        return flag1 && flag && flag2;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack traitStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty() && isTraitItem(slotStack.getItem())) {
                traitStack = slotStack;
            }
        }

        ItemStack schematicStack = new ItemStack(ModItems.UPGRADE_SCHEMATIC.get());
        schematicStack.getOrCreateTag().putString(UpgradeSchematicItem.TAG, UpgradeSchematicItem.applies(traitStack.getItem()));

        return schematicStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends SpecialRecipeSerializer<UpgradeSchematicRecipe> {
        public Serializer() {
            super(UpgradeSchematicRecipe::new);
            setRegistryName(NeoArsenal.MOD_ID, "upgrade_schematic_recipe");
        }
    }
}
