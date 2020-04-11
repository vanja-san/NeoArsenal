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
import zorochase.neoarsenal.item.SchematicItem;
import zorochase.neoarsenal.item.ToolSchematicItem;
import zorochase.neoarsenal.registry.ModItems;

public class ToolSchematicRecipe extends SpecialRecipe {

    public static final SpecialRecipeSerializer<ToolSchematicRecipe> SERIALIZER = new Serializer();

    public ToolSchematicRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    private boolean isTraitItem(Item item) {
        return (item == Items.WOODEN_SWORD || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.BLANK_SHEET.get() && !flag) {
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
        ItemStack toolStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (!slotStack.isEmpty() && isTraitItem(slotStack.getItem())) {
                toolStack = slotStack;
                break;
            }
        }

        ItemStack schematicStack = new ItemStack(ModItems.TOOL_SCHEMATIC.get(), 1);
        schematicStack.getOrCreateTag().putString(SchematicItem.TAG, ToolSchematicItem.crafts(toolStack.getItem()));

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

    private static class Serializer extends SpecialRecipeSerializer<ToolSchematicRecipe> {
        public Serializer() {
            super(ToolSchematicRecipe::new);
            setRegistryName(NeoArsenal.MOD_ID, "tool_schematic_recipe");
        }
    }

}
